package poolc.poolc.Repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Tag;
import poolc.poolc.repository.TagRepository;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
public class TagRepositoryTest {
    @Autowired
    TagRepository tagRepository;
    @Autowired
    EntityManager em;

    @Test
    public void Tag생성() throws Exception{
        //given
        Tag tag = new Tag("tag");
        //when
        tagRepository.save(tag);

        //then
        em.flush();
        em.clear();
        tag.equals(tagRepository.findOne(tag.getId()));
    }

    @Test
    public void Tag삭제() throws Exception{
        //given
        Tag tag = new Tag("tag");
        tagRepository.save(tag);


        //when
        tagRepository.delete(tag.getId());
        //then
        em.flush();
        em.clear();
        Assertions.assertNull(tagRepository.findOne(tag.getId()));
    }

    @Test
    public void 전체tag조회() throws Exception{
        //given
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");

        //when
        tagRepository.save(tag1);
        tagRepository.save(tag2);
        //then
        em.flush();
        em.clear();
        List<Tag> tagList = tagRepository.findAll();
        Assertions.assertEquals(2L, tagList.size());
    }
}
