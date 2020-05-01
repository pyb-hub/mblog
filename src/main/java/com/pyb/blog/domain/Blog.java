package com.pyb.blog.domain;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*博客类：jpa加entity自动生成表*/
@Entity
@Table(name = "t_blog")
public class Blog {

    /*主键自增*/
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    /*大资源懒加载*/
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;
    private String firstPicture;
    /*标记原创、转载*/
    private String flag;
    /*浏览数*/
    private Integer view;
    /*赞赏，转载声明，评论，推荐是否打开*/
    private boolean appreciation;
    private boolean shareContent;
    private boolean commentabled;
    private boolean recommend;
    /*发布还是保存草稿*/
    private boolean published;
    /*创建或者更新时间*/
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    /*博客放在首页的描述内容*/
    private String description;

    /*外键关系*/
    @ManyToOne
    private Type type;
    /*cascade：关联的持久化策略，
    * CascadeType.REMOVE：级联删除操作。
      删除当前实体时，与它有映射关系的实体也会跟着被删除。

      CascadeType.MERGE：级联更新（合并）操作。
      当Student中的数据改变，会相应地更新Course中的数据。

      CascadeType.DETACH：级联脱管/游离操作。
      如果你要删除一个实体，但是它有外键无法删除，你就需要这个级联权限了。它会撤销所有相关的外键关联。

      CascadeType.REFRESH：级联刷新操作。
      假设场景 有一个订单,订单里面关联了许多商品,这个订单可以被很多人操作,那么这个时候A对此订单和关联的商品进行了修改,与此同时,B也进行了相同的操作,但是B先一步比A保存了数据,那么当A保存数据的时候,就需要先刷新订单信息及关联的商品信息后,再将订单及商品保存。(来自良心会痛的评论)

      CascadeType.ALL：清晰明确，拥有以上所有级联操作权限。*/
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Tag> tags = new ArrayList<>();
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();


    //不会进入数据库，用于接收前端返回tagId的字符串值（默认多个值前端用"，"隔开）
    @Transient
    private String tagIds;
    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public boolean isAppreciation() {
        return appreciation;
    }

    public void setAppreciation(boolean appreciation) {
        this.appreciation = appreciation;
    }

    public boolean isShareContent() {
        return shareContent;
    }

    public void setShareContent(boolean shareContent) {
        this.shareContent = shareContent;
    }

    public boolean isCommentabled() {
        return commentabled;
    }

    public void setCommentabled(boolean commentabled) {
        this.commentabled = commentabled;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*把tags集合转化为tagIds字符串，传入前端页面,在重新编辑博客的时候用到，需要把数据库里面的tags在前端显示*/
    public void init() {
        this.tagIds = tagsToIds(this.getTags());
    }

    //1,2,3
    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", view=" + view +
                ", appreciation=" + appreciation +
                ", shareContent=" + shareContent +
                ", commentabled=" + commentabled +
                ", recommend=" + recommend +
                ", published=" + published +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", tags=" + tags +
                ", user=" + user +
                ", comments=" + comments +
                ", tagIds='" + tagIds + '\'' +
                '}';
    }
}
