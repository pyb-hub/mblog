package com.pyb.blog.domain;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*评论类*/
@Entity
@Table(name = "t_comment")
public class Comment {

    /*主键自增*/
    @Id
    @GeneratedValue
    private Long id;
    private String nickname;
    private Boolean adminComment;
    private String content;
    /*头像*/
    private String avatar;
    /*时间的格式*/
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @ManyToOne
    private Blog blog;

    /*内部多级评论间的关系*/
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComments = new ArrayList<>();
    @ManyToOne
    private Comment parentComment;


    public Comment() {
    }

    public List<Comment> getChildComments() {
        return childComments;
    }

    public void setChildComments(List<Comment> childComments) {
        this.childComments = childComments;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(Boolean adminComment) {
        this.adminComment = adminComment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", adminComment=" + adminComment +
                ", content='" + content + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", blog=" + blog +
                ", childComments=" + childComments +
                ", parentComment=" + parentComment +
                '}';
    }
}
