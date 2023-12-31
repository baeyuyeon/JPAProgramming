package jpabook.start.chapter7_3_5;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
public class BoardDetail {

    @Id
    private Long boardId;

    @MapsId //BoardDetail.boardId 와 매핑됨
    @OneToOne
    @JoinColumn(name = "BOARD_ID")
    private Board2 board;

    private String content;

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Board2 getBoard() {
        return board;
    }

    public void setBoard(Board2 board) {
        this.board = board;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
