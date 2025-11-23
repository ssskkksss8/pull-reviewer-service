package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void fullFlow_matchesOpenApiAndBusinessLogic() {
        String teamName = "backend";
        String userAliceId = UUID.randomUUID().toString();
        String userBobId = UUID.randomUUID().toString();
        String prId = UUID.randomUUID().toString();

        Map<String, Object> teamBody = Map.of(
                "team_name", teamName,
                "members", List.of(
                        Map.of("user_id", userAliceId, "username", "Alice", "is_active", true),
                        Map.of("user_id", userBobId, "username", "Bob", "is_active", true)
                )
        );

        ResponseEntity<Map> createTeamResp = restTemplate.postForEntity(
                "/team/add",
                teamBody,
                Map.class
        );

        assertThat(createTeamResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createTeamResp.getBody()).isNotNull();
        Map<?, ?> team = (Map<?, ?>) createTeamResp.getBody().get("team");
        assertThat(team.get("team_name")).isEqualTo(teamName);

        ResponseEntity<Map> getTeamResp = restTemplate.getForEntity(
                "/team/get?team_name=" + teamName,
                Map.class
        );
        assertThat(getTeamResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getTeamResp.getBody()).isNotNull();
        assertThat(getTeamResp.getBody().get("team_name")).isEqualTo(teamName);

        Map<String, Object> setInactiveBody = Map.of(
                "user_id", userBobId,
                "is_active", false
        );

        ResponseEntity<Map> setInactiveResp = restTemplate.postForEntity(
                "/users/setIsActive",
                setInactiveBody,
                Map.class
        );

        assertThat(setInactiveResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<?, ?> user = (Map<?, ?>) setInactiveResp.getBody().get("user");
        assertThat(user.get("user_id")).isEqualTo(userBobId);
        assertThat(user.get("is_active")).isEqualTo(false);

        Map<String, Object> createPrBody = Map.of(
                "pull_request_id", prId,
                "pull_request_name", "Add search",
                "author_id", userAliceId
        );

        ResponseEntity<Map> createPrResp = restTemplate.postForEntity(
                "/pullRequest/create",
                createPrBody,
                Map.class
        );

        assertThat(createPrResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createPrResp.getBody()).isNotNull();

        Map<?, ?> pr = (Map<?, ?>) createPrResp.getBody().get("pr");
        assertThat(pr.get("pull_request_id")).isEqualTo(prId);
        assertThat(pr.get("status")).isEqualTo("OPEN");

        ResponseEntity<Map> reviewsResp = restTemplate.getForEntity(
                "/users/getReview?user_id=" + userAliceId,
                Map.class
        );

        assertThat(reviewsResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reviewsResp.getBody()).isNotNull();
        assertThat(reviewsResp.getBody().get("user_id")).isEqualTo(userAliceId);

        Map<String, Object> mergeBody = Map.of(
                "pull_request_id", prId
        );

        ResponseEntity<Map> mergeResp = restTemplate.postForEntity(
                "/pullRequest/merge",
                mergeBody,
                Map.class
        );

        assertThat(mergeResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<?, ?> prAfterMerge = (Map<?, ?>) mergeResp.getBody().get("pr");
        assertThat(prAfterMerge.get("status")).isEqualTo("MERGED");
    }
}
