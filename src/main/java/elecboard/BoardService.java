package elecboard;

import elecboard.DTO.Sub.Payload;
import elecboard.DTO.Dashboard;
import elecboard.DTO.WhiteboardObjects.ImageObject;
import elecboard.DTO.WhiteboardObjects.WhiteboardObject;
import lombok.RequiredArgsConstructor;
import elecboard.DTO.PageDocument;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final S3Service s3Service;

    public PageDocument saveOrUpdatePage(PageDocument page) {
        PageDocument existing = boardRepository.findByRoomId(page.getRoomId());

        //기존 roomId가 존재하면 업데이트, 아니면 새롭게 저장
        PageDocument doc;
        if(existing != null){
            doc = existing;
            //이미지 url이 바뀌었는지도 check해야 함
            Set<String> oldUrls = extractImageUrls(doc.getObjects());
            Set<String> newUrls = extractImageUrls(page.getObjects());
            //checkImage(기존 이미지 url들, 현재 이미지 url들);
            checkImageChanges(oldUrls, newUrls);
        } else{
            doc = new PageDocument();
        }
        doc.setRoomId(page.getRoomId());
        doc.setMadeBy(page.getMadeBy());
        doc.setRoomName(page.getRoomName());
        doc.setParticipants(page.getParticipants());
        doc.setCreatedAt(page.getCreatedAt());
        doc.setMode(page.getMode());
        if(page.getObjects() == null){
            List<WhiteboardObject> emptyList = new ArrayList<>();
            doc.setObjects(emptyList);
        }else{
            doc.setObjects(page.getObjects());
        }

        return boardRepository.save(doc); // 저장 or 업데이트
    }

    //이전 이미지 set과 새로운 이미지 set 생성
    private Set<String> extractImageUrls(List<WhiteboardObject> objects) {
        return objects.stream()
                .filter(obj -> obj instanceof ImageObject)
                .map(obj -> ((ImageObject) obj).getPayload())
                .filter(Objects::nonNull)
                .map(Payload::getSrc)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    //삭제된 이미지 url 파악 후 s3에서 삭제
    @Async
    public void checkImageChanges(Set<String> oldUrls, Set<String> newUrls) {
        // 삭제된 이미지: old에는 있고 new에는 없는 경우
        Set<String> removed = new HashSet<>(oldUrls);
        removed.removeAll(newUrls);

        // 새로 추가된 이미지: new에는 있고 old에는 없는 경우
        Set<String> added = new HashSet<>(newUrls);
        added.removeAll(oldUrls);

        if (!removed.isEmpty()) {
            for (String url : removed) {
                s3Service.deleteByUrl(url);  // 예: S3에서 url로 삭제
            }
        }
    }

    public List<Dashboard> getBoardsByUser(int userId) {
        List<PageDocument> pages = boardRepository.findByParticipants_Id(userId);
        return pages.stream()
                .map(p -> new Dashboard(p.getRoomId(), p.getRoomName(), p.getParticipants()))
                .collect(Collectors.toList());
    }

    public Optional<PageDocument> getPageByRoomId(String roomId, int userId) {
        Optional<PageDocument> doc = boardRepository.findByRoomIdAndParticipants_Id(roomId, userId);
        if (doc.isEmpty()) return Optional.empty();

        PageDocument page = new PageDocument();
        page.setRoomId(doc.get().getRoomId());
        page.setRoomName(doc.get().getRoomName());
        page.setParticipants(doc.get().getParticipants());
        page.setMadeBy(doc.get().getMadeBy());
        page.setCreatedAt(doc.get().getCreatedAt());
        page.setMode(doc.get().getMode());
        page.setObjects(doc.get().getObjects());

        return Optional.of(page);
    }

    public boolean deletePageByRoomId(String roomId, int userId) {
        Optional<PageDocument> pageOpt = boardRepository.findByRoomIdAndParticipants_Id(roomId, userId);

        //방장만 삭제할 수 있도록 하고싶은 경우
//        if (!pageOpt.get().getCreatedBy().equals(userName)) {
//            return false;
//        }
        if (pageOpt.isEmpty()) {
            return false;
        }
        // 삭제 전 이미지 URL 추출
        PageDocument page = pageOpt.get();
        List<WhiteboardObject> objects = page.getObjects();
        if (objects != null) {
            List<String> imageUrls = objects.stream()
                    .filter(obj -> obj instanceof ImageObject)
                    .map(obj -> ((ImageObject) obj).getPayload())
                    .filter(Objects::nonNull)
                    .map(Payload::getSrc)
                    .filter(Objects::nonNull)
                    .toList();

            // S3에서 이미지 삭제
            for (String url : imageUrls) {
                s3Service.deleteByUrl(url);
            }
        }

        boardRepository.deleteByRoomIdAndParticipants_Id(roomId, userId);
        return true;
    }
}
