package andreyz.agent.service.persist;

import andreyz.agent.domain.CoverLetter.CoverLetterResponse;
import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.resumeMatcher.MatchResult;
import andreyz.agent.dto.ParserServiceType;
import andreyz.agent.persistence.dto.VacancyProcessingResult;
import andreyz.agent.persistence.entity.VacancySnapshotEntity;
import andreyz.agent.persistence.mapper.CoverLetterMapper;
import andreyz.agent.persistence.mapper.MatchResultMapper;
import andreyz.agent.persistence.mapper.VacancyMapper;
import andreyz.agent.persistence.repo.CoverLetterSnapshotRepository;
import andreyz.agent.persistence.repo.MatchResultSnapshotRepository;
import andreyz.agent.persistence.repo.VacancySnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class VacancyPersistService {

    private final VacancySnapshotRepository vacancyRepository;
    private final MatchResultSnapshotRepository matchResultRepository;
    private final CoverLetterSnapshotRepository coverLetterRepository;


    @Value("${spring.ai.model.chat:NA}")
    private String modelName;

    @Transactional
    public void persist(VacancyProcessingResult processingResult) {

        Vacancy vacancy = processingResult.vacancy();
        MatchResult matchResult = processingResult.matchResult();
        CoverLetterResponse coverLetter = processingResult.coverLetter();
        ParserServiceType parserServiceType = processingResult.parserServiceType();


        OffsetDateTime receivedAt = OffsetDateTime.now();

        VacancySnapshotEntity vacancySnapshot = vacancyRepository.save(
                VacancyMapper.toEntity(
                        vacancy,
                        parserServiceType.name(),
                        receivedAt
                )
        );

        matchResultRepository.save(
                MatchResultMapper.toEntity(
                        vacancySnapshot.getId(),
                        matchResult,
                        "v1",
                        receivedAt
                )
        );

        coverLetterRepository.save(
                CoverLetterMapper.toEntity(
                        vacancySnapshot.getId(),
                        coverLetter,
                        modelName,
                        "v1",
                        receivedAt
                )
        );
    }
}
