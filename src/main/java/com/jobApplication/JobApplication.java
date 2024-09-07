package com.jobApplication;

import com.github.javafaker.Faker;
import com.jobApplication.company.Company;
import com.jobApplication.company.CompanyRepository;
import com.jobApplication.dao.JobRepository;
import com.jobApplication.model.Job;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class JobApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobApplication.class, args);
	}


	//@Bean
	public CommandLineRunner commandLineRunner(JobRepository jobRepository, CompanyRepository companyRepository){
		return args -> {
			Faker faker = new Faker();

			// Create and save 100 companies
			for (int i = 0; i < 100; i++) {
				Company company = Company.builder()
						.name(faker.company().name())
						.description(faker.company().industry())
						.build();
				companyRepository.save(company);
			}

			// Retrieve all companies from the database
			List<Company> allCompanies = companyRepository.findAll();

			// Create and save 100 jobs, associating each job with a random company
			for (int i = 0; i < 100; i++) {
				// Randomly select a company from the list of all companies
				Company randomCompany = allCompanies.get(faker.random().nextInt(allCompanies.size()));

				// Create and save a sample job associated with the randomly selected company
				Job job = Job.builder()
						.title(faker.job().title())
						.description(faker.job().position())
						.minSalary(String.valueOf(faker.number().numberBetween(50000, 80000)))
						.maxSalary(String.valueOf(faker.number().numberBetween(80000, 120000)))
						.location(faker.address().city())
						.company(randomCompany)
						.build();
				jobRepository.save(job);
			}
		};
	}
	/*@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService service){
		return args -> {
			RegisterRequest admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("AdminToken : " + service.register(admin).getAccessToken());

			RegisterRequest manager = RegisterRequest.builder()
					.firstname("manager")
					.lastname("manager")
					.email("manager@mail.com")
					.password("password")
					.role(MANAGER)
					.build();
			System.out.println("ManagerToken : " + service.register(manager).getAccessToken());
		};
*/
	}

