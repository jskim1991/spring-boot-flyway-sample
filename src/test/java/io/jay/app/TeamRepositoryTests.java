package io.jay.app;

import io.jay.app.initializer.TestContainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainerConfiguration.class)
public class TeamRepositoryTests {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void test_teamAndMembers() {
        var team = new TeamEntity("Labs");

        var firstMember = MemberEntity.builder()
                .name("Jay")
                .email("jay@labs.com")
                .team(team)
                .build();
        var secondMember = MemberEntity.builder()
                .name("Joel")
                .email("joel@labs.com")
                .team(team)
                .build();
        var thirdMember = MemberEntity.builder()
                .name("Junhyunny")
                .email("junhyunny@labs.com")
                .team(team)
                .build();
        var fourthMember = MemberEntity.builder()
                .name("Steve")
                .email("steve@labs.com")
                .team(team)
                .build();

        team.setMembers(List.of(firstMember, secondMember, thirdMember, fourthMember));


        var saved = teamRepository.save(team);


        var fetchedTeam = teamRepository.findById(saved.getId()).get();


        assertEquals("Labs", fetchedTeam.getName());
        assertEquals(4, fetchedTeam.getMembers().size());
        assertEquals("Jay", fetchedTeam.getMembers().get(0).getName());
        assertEquals("Joel", fetchedTeam.getMembers().get(1).getName());
        assertEquals("Junhyunny", fetchedTeam.getMembers().get(2).getName());
        assertEquals("Steve", fetchedTeam.getMembers().get(3).getName());
    }
}
