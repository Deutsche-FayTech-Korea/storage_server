package elecboard;

import elecboard.DTO.Sub.Payload;
import elecboard.DTO.WhiteboardObjects.ImageObject;
import elecboard.DTO.WhiteboardObjects.WhiteboardObject;
import lombok.RequiredArgsConstructor;
import elecboard.DTO.Page;
import elecboard.DTO.PageDocument;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public PageDocument saveOrUpdatePage(Page page) {
        PageDocument existing = boardRepository.findByRoomId(page.getRoomId());

        //기존 roomId가 존재하면 업데이트, 아니면 새롭게 저장
        PageDocument doc;
        if(existing != null){
            doc = existing;
            //이미지 url이 바뀌었는지도 check해야 함

        } else{
            doc = new PageDocument();
        }
        doc.setRoomId(page.getRoomId());
        doc.setRoomName(page.getRoomName());
        doc.setUserNames(page.getUserNames());
        doc.setObjects(page.getObjects());

        return boardRepository.save(doc); // 저장 or 업데이트
    }
}
