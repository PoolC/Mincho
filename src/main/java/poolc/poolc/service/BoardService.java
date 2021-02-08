package poolc.poolc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Board;
import poolc.poolc.domain.Comment;
import poolc.poolc.domain.Post;
import poolc.poolc.repository.BoardRepository;
import poolc.poolc.repository.CommentRepository;
import poolc.poolc.repository.PostRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    @Autowired
    private final BoardRepository boardRepository;

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final CommentRepository commentRepository;

    @Transactional
    public void saveBoard(Board board){
        boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long boardID){
        Board findBoard = boardRepository.findOne(boardID);
        List<Post> postList =  postRepository.findAllByBoard(findBoard);

        for (Post post : postList) {
            List<Comment> commentList = commentRepository.findAllByPost(post.getId());
            for (Comment comment : commentList) {
                commentRepository.delete(comment.getId());
            }
            postRepository.delete(post.getId());
        }
        boardRepository.delete(findBoard);
    }

    public List<Board> findBoards(){
        return boardRepository.findAll();
    }

    public Board findOneBoard(Long BoardID){
        return boardRepository.findOne(BoardID);
    }

    @Transactional
    public void update(Long boardID, String name, String URLPath, String readPermission, String writePermission){
        Board findBoard = boardRepository.findOne(boardID);
        findBoard.updateBoard(name, URLPath, readPermission, writePermission);
    }

}
