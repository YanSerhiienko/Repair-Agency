package com.bitsva.RepairAgency;

import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import com.bitsva.RepairAgency.feature.UserRole;
import com.bitsva.RepairAgency.repository.RepairRequestRepository;
import com.bitsva.RepairAgency.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RepairAgencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepairAgencyApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(RepairRequestRepository requestRepository, UserRepository userRepository) {
		return args -> {
			User user = new User();
			user.setId(1L);
			user.setFirstName("Optimus");
			user.setLastName("Prime");
			user.setEmail("prime@mail.com");
			user.setPhone("0999999999");
			user.setRole(UserRole.ROLE_ADMIN);

			RepairRequest repairRequest = new RepairRequest();
			//repairRequest.setId(1L);
			repairRequest.setCreationDate("2023-07-13");
			repairRequest.setDescription("repair plz");
			repairRequest.setRepairer("Unknown Stranger");
			repairRequest.setCost(10L);
			repairRequest.setCompletionStatus(RepairRequestCompletionStatus.NOT_STARTED);
			repairRequest.setPaymentStatus(RepairRequestPaymentStatus.CANCELED);

			//repairRequest.setClient(user);

			user.getRequests().add(repairRequest);
			userRepository.save(user);
		};
	}

	/*@Bean
	public CommandLineRunner loadData(RepairRequestRepository requestRepository) {
		return args -> {
			RepairRequest r1 = new RepairRequest();
			r1.setCreationDate("2023-07-06");
			r1.setDescription("repair plz");
			r1.setRepairer("Unknown Stranger");
			r1.setCost(10L);
			r1.setCompletionStatus(RepairRequestCompletionStatus.NOT_STARTED);
			r1.setPaymentStatus(RepairRequestPaymentStatus.CANCELED);
			requestRepository.save(r1);

			RepairRequest r2 = new RepairRequest();
			r2.setCreationDate("2023-07-06");
			r2.setDescription("repair plz");
			r2.setRepairer("Unknown Stranger");
			r2.setCost(40L);
			r2.setCompletionStatus(RepairRequestCompletionStatus.NOT_STARTED);
			r2.setPaymentStatus(RepairRequestPaymentStatus.AWAITING_PAYMENT);
			requestRepository.save(r2);

			RepairRequest r3 = new RepairRequest();
			r3.setCreationDate("2023-07-06");
			r3.setDescription("repair plz");
			r3.setRepairer("Unknown Stranger");
			r3.setCost(33L);
			r3.setCompletionStatus(RepairRequestCompletionStatus.NOT_STARTED);
			r3.setPaymentStatus(RepairRequestPaymentStatus.AWAITING_PAYMENT);
			requestRepository.save(r3);

			RepairRequest r4 = new RepairRequest();
			r4.setCreationDate("2023-07-06");
			r4.setDescription("repair plz");
			r4.setRepairer("Unknown Stranger");
			r4.setCost(11L);
			r4.setCompletionStatus(RepairRequestCompletionStatus.NOT_STARTED);
			r4.setPaymentStatus(RepairRequestPaymentStatus.PAID);
			requestRepository.save(r4);

			RepairRequest r5 = new RepairRequest();
			r5.setCreationDate("2023-07-06");
			r5.setDescription("repair plz");
			r5.setRepairer("Unknown Stranger");
			r5.setCost(13L);
			r5.setCompletionStatus(RepairRequestCompletionStatus.NOT_STARTED);
			r5.setPaymentStatus(RepairRequestPaymentStatus.PAID);
			requestRepository.save(r5);

			RepairRequest r6 = new RepairRequest();
			r6.setCreationDate("2023-07-06");
			r6.setDescription("repair plz");
			r6.setRepairer("Unknown Stranger");
			r6.setCost(14L);
			r6.setCompletionStatus(RepairRequestCompletionStatus.IN_PROGRESS);
			r6.setPaymentStatus(RepairRequestPaymentStatus.PAID);
			requestRepository.save(r6);

			RepairRequest r7 = new RepairRequest();
			r7.setCreationDate("2023-07-06");
			r7.setDescription("repair plz");
			r7.setRepairer("Unknown Stranger");
			r7.setCost(12L);
			r7.setCompletionStatus(RepairRequestCompletionStatus.IN_PROGRESS);
			r7.setPaymentStatus(RepairRequestPaymentStatus.PAID);
			requestRepository.save(r7);

			RepairRequest r8 = new RepairRequest();
			r8.setCreationDate("2023-07-06");
			r8.setDescription("repair plz");
			r8.setRepairer("Unknown Stranger");
			r8.setCost(234L);
			r8.setCompletionStatus(RepairRequestCompletionStatus.IN_PROGRESS);
			r8.setPaymentStatus(RepairRequestPaymentStatus.PAID);
			requestRepository.save(r8);

			RepairRequest r9 = new RepairRequest();
			r9.setCreationDate("2023-07-06");
			r9.setDescription("repair plz");
			r9.setRepairer("Unknown Stranger");
			r9.setCost(10L);
			r9.setCompletionStatus(RepairRequestCompletionStatus.COMPLETED);
			r9.setPaymentStatus(RepairRequestPaymentStatus.PAID);
			requestRepository.save(r9);

			RepairRequest r10 = new RepairRequest();
			r10.setCreationDate("2023-07-06");
			r10.setDescription("repair plz");
			r10.setRepairer("Unknown Stranger");
			r10.setCost(54L);
			r10.setCompletionStatus(RepairRequestCompletionStatus.COMPLETED);
			r10.setPaymentStatus(RepairRequestPaymentStatus.PAID);
			requestRepository.save(r10);

			RepairRequest r11 = new RepairRequest();
			r11.setCreationDate("2023-07-06");
			r11.setDescription("repair plz");
			r11.setRepairer("Unknown Stranger");
			r11.setCost(23L);
			r11.setCompletionStatus(RepairRequestCompletionStatus.COMPLETED);
			r11.setPaymentStatus(RepairRequestPaymentStatus.PAID);
			requestRepository.save(r11);

		};
	}*/
}
