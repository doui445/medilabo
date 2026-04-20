package org.medilabo.patient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.medilabo.patient.model.Gender;
import org.medilabo.patient.model.Patient;
import org.medilabo.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PatientApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PatientRepository patientRepository;

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("Should display the patient list JSON with updated data from database")
	void patientIntegrationTest() throws Exception {
		Patient patient = Patient.builder()
				.firstname("Patient")
				.lastname("Test")
				.birthDate(LocalDate.now())
				.gender(Gender.M)
				.address("123 Test")
				.phone("0123456789")
				.build();

		// Save
		patient = patientRepository.save(patient);
		assertNotNull(patient.getId());
		assertEquals("Patient", patient.getFirstname());

		// Update
		patient.setGender(Gender.F);
		patient = patientRepository.save(patient);
		assertNotNull(patient.getId());
		assertEquals("F", patient.getGender().name());

		mockMvc.perform(get("/api/patients"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Patient")))
				.andExpect(content().string(containsString("F")))
				.andExpect(content().string(containsString("0123456789")));

		// Find
		List<Patient> listResult = patientRepository.findAll();
		assertFalse(listResult.isEmpty());

		// Delete
		Long id = patient.getId();
		patientRepository.delete(patient);
		Optional<Patient> optionalPatient = patientRepository.findById(id);
		assertFalse(optionalPatient.isPresent());
	}
}
