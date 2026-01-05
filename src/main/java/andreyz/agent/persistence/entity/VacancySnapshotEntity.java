package andreyz.agent.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("vacancy_snapshot")
@Data
public class VacancySnapshotEntity {

    @Id
    private UUID id;

    @Column("company_name")
    private String companyName;

    @Column("position_title")
    private String positionTitle;

    @Column("salary")
    private Long salary;

    @Column("region")
    private String region;

    @Column("vacancy_text")
    private String vacancyText;

    @Column("source")
    private String source;

    @Column("source_id")
    private String sourceId;

    @Column("mail_source")
    private String mailSource;

    @Column("received_at")
    private OffsetDateTime receivedAt;

    @Column("created_at")
    private OffsetDateTime createdAt;

    public VacancySnapshotEntity(UUID id, String companyName, String positionTitle, Long salary, String region, String vacancyText, String source, String sourceId, String mailSource, OffsetDateTime receivedAt, OffsetDateTime createdAt) {
        this.id = id;
        this.companyName = companyName;
        this.positionTitle = positionTitle;
        this.salary = salary;
        this.region = region;
        this.vacancyText = vacancyText;
        this.source = source;
        this.sourceId = sourceId;
        this.mailSource = mailSource;
        this.receivedAt = receivedAt;
        this.createdAt = createdAt;
    }
}
