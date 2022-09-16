package amacic.repo.PostTagRepo;

public interface PostTagRepository {
    public void addPostTag(long postId, long tagId);

    public void removePostFromTag(long postId);
}
