package service;

import model.Interest;
import repository.InterestRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class InterestService {
    private final InterestRepository interestRepository;

    public InterestService(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    
    @Scheduled(cron = "0 26 2 * * ?") 
    @Transactional
    public void applyAccumulatedInterest() {
        List<Interest> loans = interestRepository.findAll();

        if (loans.isEmpty()) {
            System.out.println("⚠ No loans found to apply interest.");
            return;
        }

        for (Interest loan : loans) {
            // Ensure interestAmount is not null
            if (loan.getInterestAmount() == null) {
                loan.setInterestAmount(BigDecimal.ZERO);
            }

            // Calculate days since disbursal
            long daysSinceDisbursal = ChronoUnit.DAYS.between(loan.getDateOfDisbursal(), LocalDate.now());

            // Ensure we do not apply interest for future-dated loans
            if (daysSinceDisbursal < 0) {
                System.out.println("⚠ Loan ID " + loan.getId() + " has a future disbursal date. Skipping...");
                continue;
            }

            
            BigDecimal totalInterest = loan.getPrincipalAmount()
                                           .multiply(loan.getInterestRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)) // Convert rate to decimal
                                           .multiply(BigDecimal.valueOf(daysSinceDisbursal))
                                           .divide(BigDecimal.valueOf(365), 2, RoundingMode.HALF_UP);

            // Update interest amount
            loan.setInterestAmount(totalInterest);
        }

        // Batch save all updated loans to the database
        interestRepository.saveAll(loans);

        System.out.println("✅ Total accumulated interest applied successfully!");
    }
}
