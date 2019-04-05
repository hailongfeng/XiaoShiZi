package edu.children.xiaoshizi.bean;

import java.io.Serializable;
import java.util.List;

public class LoadContentCategoryResponse  implements Serializable {
    private List<ArticleType> categoryResps;
    private List<Article> contentResps;

    public List<ArticleType> getCategoryResps() {
        return categoryResps;
    }

    public void setCategoryResps(List<ArticleType> categoryResps) {
        this.categoryResps = categoryResps;
    }

    public List<Article> getContentResps() {
        return contentResps;
    }

    public void setContentResps(List<Article> contentResps) {
        this.contentResps = contentResps;
    }
}
