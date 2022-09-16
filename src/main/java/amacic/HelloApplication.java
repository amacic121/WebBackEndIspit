package amacic;

import amacic.repo.CategoryRepo.CategoryRepository;
import amacic.repo.CategoryRepo.CategoryRepositoryImplA;
import amacic.repo.CommentRepo.CommentRepository;
import amacic.repo.CommentRepo.CommentRepositoryImplA;
import amacic.repo.PostRepo.PostRepository;
import amacic.repo.PostRepo.PostRepositoryImplA;
import amacic.repo.PostTagRepo.PostTagRepository;
import amacic.repo.PostTagRepo.PostTagRepositoryImplA;
import amacic.repo.TagRepo.TagRepository;
import amacic.repo.TagRepo.TagRepositoryImplA;
import amacic.repo.UserRepo.UserRepository;
import amacic.repo.UserRepo.UserRepositoryImplA;
import amacic.services.CategoryService.CategoryServiceImpl;
import amacic.services.CommentService.CommentServiceImpl;
import amacic.services.PostService.PostServiceImpl;
import amacic.services.UserService.UserServiceImpl;
import amacic.services.CategoryService.CategoryService;
import amacic.services.CommentService.CommentService;
import amacic.services.PostService.PostService;
import amacic.services.UserService.UserService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class HelloApplication extends ResourceConfig {
    public HelloApplication() {
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                this.bind(CategoryRepositoryImplA.class).to(CategoryRepository.class).in(Singleton.class);
                this.bind(PostRepositoryImplA.class).to(PostRepository.class).in(Singleton.class);
                this.bind(TagRepositoryImplA.class).to(TagRepository.class).in(Singleton.class);
                this.bind(PostTagRepositoryImplA.class).to(PostTagRepository.class).in(Singleton.class);
                this.bind(CommentRepositoryImplA.class).to(CommentRepository.class).in(Singleton.class);
                this.bind(UserRepositoryImplA.class).to(UserRepository.class).in(Singleton.class);

                this.bindAsContract(CategoryServiceImpl.class).to(CategoryService.class).in(Singleton.class);
                this.bindAsContract(PostServiceImpl.class).to(PostService.class).in(Singleton.class);
                this.bindAsContract(CommentServiceImpl.class).to(CommentService.class).in(Singleton.class);
                this.bindAsContract(UserServiceImpl.class).to(UserService.class).in(Singleton.class);
            }
        };
        register(binder);
        packages("amacic");
    }
}