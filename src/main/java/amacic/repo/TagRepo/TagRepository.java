package amacic.repo.TagRepo;

import amacic.data.Tag;

import java.util.List;

public interface TagRepository {
    public Tag createTagIfNameNotExist(Tag tag);

    public List<Tag> findTagsByPostId(long postId);
}
