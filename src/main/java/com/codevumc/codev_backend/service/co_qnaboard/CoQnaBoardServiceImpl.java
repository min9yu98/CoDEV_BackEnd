package com.codevumc.codev_backend.service.co_qnaboard;

import com.codevumc.codev_backend.domain.*;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoPhotosMapper;
import com.codevumc.codev_backend.mapper.CoQnaBoardMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@AllArgsConstructor
@Service
public class CoQnaBoardServiceImpl extends ResponseService implements CoQnaBoardService{

    private final CoQnaBoardMapper coQnaBoardMapper;
    private final CoPhotosMapper coPhotosMapper;



    @Override
    public CoDevResponse insertCoQnaBoard(CoQnaBoard coQnaBoard) {
        try {
            this.coQnaBoardMapper.insertCoQnaBoard(coQnaBoard);

            return setResponse(200, "message", "질문게시판 글이 작성/수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }

    }

    @Override
    public void updateMainImg(String co_mainImg, long co_qnaId) {
        coQnaBoardMapper.updateCoMainImg(co_mainImg, co_qnaId);
    }

    @Override
    public CoDevResponse insertCoCommentOfQnaBoard(CoCommentOfQnaBoard coCommentOfQnaBoard) {
        try{
            this.coQnaBoardMapper.insertCoCommentOfQnaBoard(coCommentOfQnaBoard);
            return setResponse(200,"message", "질문게시판 댓글이 작성되었습니다.");
        }catch(Exception e){
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse changeMark(String co_email, long co_qnaId) {
        try{
            if(coQnaBoardMapper.getCoMarkOfQnaBoardEmail(co_email,co_qnaId) == null){
                this.coQnaBoardMapper.insertCoMarkOfQnaBoard(co_email,co_qnaId);
                return setResponse(200,"message","북마크 등록이 완료되었습니다.");
            }
            else{
                this.coQnaBoardMapper.deleteCoMarkOfQnaBoard(co_email,co_qnaId);
                return setResponse(200,"message","북마크 등록이 취소되었습니다.");
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse insertCoReCommentOfQnaBoard(CoReCommentOfQnaBoard coReCommentOfQnaBoard) {
        try {
            this.coQnaBoardMapper.insertCoReCommentOfQnaBoard(coReCommentOfQnaBoard);
            return setResponse(200, "message", "질문게시판 대댓글이 작성되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse getCoQnaBoard(String co_viewer, long co_qnaId) {
        try {
            Map<String, Object> coQnaBoardDto = new HashMap<>();
            List<CoPhotos> coPhotosList = coPhotosMapper.findByCoTargetId(String.valueOf(co_qnaId), "QNABOARD");
            coQnaBoardDto.put("co_viewer", co_viewer);
            coQnaBoardDto.put("co_qnaId", co_qnaId);
            Optional<CoQnaBoard> coQnaBoard = coQnaBoardMapper.getCoQnaBoardByViewer(coQnaBoardDto);
            if(coQnaBoard.isPresent()) {
                coQnaBoard.get().setCo_viewer(co_viewer);
                coQnaBoard.get().setCo_photos(coPhotosList);
                coQnaBoard.get().setCo_comment(coQnaBoardMapper.getComment(co_qnaId));
            }
                return setResponse(200, "Complete", coQnaBoard);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse getAllQnaBoards(String co_email, String co_keyword, String co_sortingTag, int limit, int offset, int pageNum) {
        try {
            Map<String, Object> condition = new HashMap<>();
            condition.put("co_email", co_email);
            condition.put("co_keyword", setting(co_keyword));
            condition.put("co_sortingTag", co_sortingTag);
            condition.put("limit", limit);
            condition.put("offset", offset);
            List<CoQnaBoard> coQnaBoards = this.coQnaBoardMapper.getCoQnaBoards(condition);
            setResponse(200, "success", coQnaBoards);
            return addResponse("co_page", pageNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String setting(String keyword) {
        return keyword == null ? null : "%" + keyword + "%";
    }

}
