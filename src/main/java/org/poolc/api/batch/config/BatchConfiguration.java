package org.poolc.api.batch.config;

import org.poolc.api.batch.itemprocessor.*;
import org.poolc.api.batch.listener.JobCompletionNotificationListener;
import org.poolc.api.batch.vo.board.BoardDao;
import org.poolc.api.batch.vo.board.Boards;
import org.poolc.api.batch.vo.comment.CommentDao;
import org.poolc.api.batch.vo.comment.Comments;
import org.poolc.api.batch.vo.member.MemberDao;
import org.poolc.api.batch.vo.member.Members;
import org.poolc.api.batch.vo.post.PostDao;
import org.poolc.api.batch.vo.post.Posts;
import org.poolc.api.batch.vo.project.ProjectDao;
import org.poolc.api.batch.vo.project.Projects;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.Arrays;

@Profile("!test")
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<Members> membersReader() {
        return new FlatFileItemReaderBuilder<Members>()
                .name("membersItemReader")
                .resource(new ClassPathResource("PoolcMember.csv"))
                .delimited()
                .names("uuid", "login_id", "password_hash",
                        "password_salt", "email", "phone_number", "name",
                        "department", "student_id", "is_activated", "is_admin",
                        "created_at", "updated_at", "password_reset_token",
                        "password_reset_token_valid_until")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Members>() {{
                    setTargetType(Members.class);
                }})
                .build();
    }

    @Bean
    public FlatFileItemReader<Boards> boardsReader() {
        return new FlatFileItemReaderBuilder<Boards>()
                .name("boardsItemReader")
                .resource(new ClassPathResource("PoolcBoard.csv"))
                .delimited()
                .names("id", "name", "url_path", "read_permission", "write_permission", "created_at", "updated_at")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Boards>() {{
                    setTargetType(Boards.class);
                }})
                .build();
    }

    @Bean
    public FlatFileItemReader<Posts> postsReader() {
        return new FlatFileItemReaderBuilder<Posts>()
                .name("postsItemReader")
                .resource(new ClassPathResource("PoolcPost.csv"))
                .recordSeparatorPolicy(new DefaultRecordSeparatorPolicy())
                .delimited()
                .names(new String[]{"id", "board_id", "author_uuid", "title", "body", "vote_id", "created_at", "updated_at"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Posts.class);
                }})
                .build();
    }

    @Bean
    public FlatFileItemReader<Comments> commentsReader() {
        return new FlatFileItemReaderBuilder<Comments>()
                .name("commentsItemReader")
                .resource(new ClassPathResource("PoolcComment.csv"))
                .delimited()
                .names("id", "post_id", "author_uuid", "body", "created_at", "updated_at")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Comments.class);
                }})
                .build();
    }

    @Bean
    public FlatFileItemReader<Projects> projectsReader() {
        return new FlatFileItemReaderBuilder<Projects>()
                .name("projectsItemReader")
                .resource(new ClassPathResource("PoolcProject.csv"))
                .recordSeparatorPolicy(new DefaultRecordSeparatorPolicy())
                .delimited()
                .names("id", "name", "genre", "thumbnail_url", "body", "created_at", "updated_at", "participants", "duration", "description")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Projects>() {{
                    setTargetType(Projects.class);
                }})
                .build();
    }

    @Bean
    public MembersItemProcessor membersProcessor() {
        return new MembersItemProcessor();
    }

    @Bean
    public BoardsItemProcessor boardsProcessor() {
        return new BoardsItemProcessor();
    }

    @Bean
    public PostsItemProcessor postsProcessor() {
        return new PostsItemProcessor();
    }

    @Bean
    public CommentsItemProcessor commentsProcessor() {
        return new CommentsItemProcessor();
    }

    @Bean
    public ProjectsItemProcessor projectsProcessor() {
        return new ProjectsItemProcessor();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<MemberDao> memberWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<MemberDao>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO member(uuid, login_id, password_hash, email, phone_number, name, department, student_id, password_reset_token, password_reset_token_valid_until, profile_image_url, introduction, is_excepted, created_at, updated_at) " +
                        "VALUES (:uuid, :login_id, :password_hash, :email, :phone_number, :name, :department, :student_id, :password_reset_token, :password_reset_token_valid_until, :profile_image_url, :introduction, :is_excepted, :created_at, :updated_at);")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<MemberDao> roleWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<MemberDao>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO roles (member_uuid, roles) VALUES (:uuid, :roles)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    @StepScope
    public CompositeItemWriter<MemberDao> compositeMemberWriter(
            @Qualifier("memberWriter") JdbcBatchItemWriter<MemberDao> memberWriter,
            @Qualifier("roleWriter") JdbcBatchItemWriter<MemberDao> roleWriter) {
        return new CompositeItemWriterBuilder<MemberDao>()
                .delegates(Arrays.asList(memberWriter, roleWriter))
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<BoardDao> boardWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<BoardDao>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO board(id, name, url_path, read_permission, write_permission, created_at, updated_at)" +
                        "VALUES (:id, :name, :url_path, :read_permission, :write_permission, :created_at, :updated_at)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<PostDao> postWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<PostDao>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO post(id, created_at, updated_at, body, title, board_id, author_uuid)" +
                        "VALUES (:id, :created_at, :updated_at, :body, :title, :board_id, :author_uuid)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<CommentDao> commentWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CommentDao>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO comment(id, post_id, author_uuid, body, created_at, updated_at)" +
                        "VALUES (:id, :post_id, :author_uuid, :body, :created_at, :updated_at)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<ProjectDao> projectWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<ProjectDao>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO project(id, created_at, updated_at, body, description, duration, genre, name, thumbnail_url)" +
                        "VALUES (:id, :created_at, :updated_at, :body, :description, :duration ,:genre, :name, :thumbnail_url)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importMembersJob(JobCompletionNotificationListener listener, Step memberStep) {
        return jobBuilderFactory.get("importMembersJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(memberStep)
                .end()
                .build();
    }

    @Bean
    public Job importBoardsJob(JobCompletionNotificationListener listener, Step boardStep) {
        return jobBuilderFactory.get("importBoardsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(boardStep)
                .end()
                .build();
    }

    @Bean
    public Job importPostsJob(JobCompletionNotificationListener listener, Step postStep) {
        return jobBuilderFactory.get("importPostsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(postStep)
                .end()
                .build();
    }

    @Bean
    public Job importCommentsJob(JobCompletionNotificationListener listener, Step commentStep) {
        return jobBuilderFactory.get("importCommentsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(commentStep)
                .end()
                .build();
    }

    @Bean
    public Job importProjectsJob(JobCompletionNotificationListener listener, Step projectStep) {
        return jobBuilderFactory.get("importProjectsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(projectStep)
                .end()
                .build();
    }

    @Bean
    public Step memberStep(CompositeItemWriter<MemberDao> compositeItemWriter) {
        return stepBuilderFactory.get("memberStep")
                .<Members, MemberDao>chunk(100)
                .reader(membersReader())
                .processor(membersProcessor())
                .writer(compositeItemWriter)
                .build();
    }

    @Bean
    public Step boardStep(JdbcBatchItemWriter<BoardDao> boardWriter) {
        return stepBuilderFactory.get("boardStep")
                .<Boards, BoardDao>chunk(10)
                .reader(boardsReader())
                .processor(boardsProcessor())
                .writer(boardWriter)
                .build();
    }

    @Bean
    public Step postStep(JdbcBatchItemWriter<PostDao> postWriter) {
        return stepBuilderFactory.get("postStep")
                .<Posts, PostDao>chunk(300)
                .reader(postsReader())
                .processor(postsProcessor())
                .writer(postWriter)
                .build();
    }

    @Bean
    public Step commentStep(JdbcBatchItemWriter<CommentDao> commentWriter) {
        return stepBuilderFactory.get("commentStep")
                .<Comments, CommentDao>chunk(300)
                .reader(commentsReader())
                .processor(commentsProcessor())
                .writer(commentWriter)
                .build();
    }

    @Bean
    public Step projectStep(JdbcBatchItemWriter<ProjectDao> projectWriter) {
        return stepBuilderFactory.get("projectStep")
                .<Projects, ProjectDao>chunk(10)
                .reader(projectsReader())
                .processor(projectsProcessor())
                .writer(projectWriter)
                .build();
    }
}
