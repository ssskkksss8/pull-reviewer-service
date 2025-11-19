
CREATE TABLE team (
                      id      UUID PRIMARY KEY,
                      name    VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users (
                       id         UUID PRIMARY KEY,
                       name       VARCHAR(255) NOT NULL,
                       is_active  BOOLEAN      NOT NULL,
                       team_id    UUID         NOT NULL,
                       CONSTRAINT fk_users_team
                           FOREIGN KEY (team_id) REFERENCES team(id)
);

CREATE TABLE pull_request (
                              id          UUID PRIMARY KEY,
                              title       VARCHAR(255) NOT NULL,
                              author_id   UUID         NOT NULL,
                              status      VARCHAR(32)  NOT NULL,
                              created_at  TIMESTAMP    NOT NULL,

                              CONSTRAINT fk_pr_author
                                  FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE reviewer_assignment (
                                     id           UUID PRIMARY KEY,
                                     pr_id        UUID NOT NULL,
                                     reviewer_id  UUID NOT NULL,

                                     CONSTRAINT fk_assignment_pr
                                         FOREIGN KEY (pr_id) REFERENCES pull_request(id),

                                     CONSTRAINT fk_assignment_reviewer
                                         FOREIGN KEY (reviewer_id) REFERENCES users(id),

                                     CONSTRAINT uq_reviewer_per_pr UNIQUE (pr_id, reviewer_id)
);

CREATE INDEX idx_users_team_id ON users(team_id);
CREATE INDEX idx_pr_author_id ON pull_request(author_id);
CREATE INDEX idx_assignment_pr_id ON reviewer_assignment(pr_id);
CREATE INDEX idx_assignment_reviewer_id ON reviewer_assignment(reviewer_id);
