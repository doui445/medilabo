package org.medilabo.notes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.medilabo.notes.model.Note;
import org.medilabo.notes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotesApplicationTests {

	@Autowired
    private MockMvc mockMvc;

	@Autowired
	private NoteRepository noteRepository;

	@Test
	void contextLoads() {
	}

	@AfterEach
	void tearDown() {
		noteRepository.deleteAll();
	}

	@Test
	@DisplayName("Should display the note list JSON with updated data from database")
	void noteIntegrationTest() throws Exception {
		Note note = Note.builder()
				.patientId(1L)
				.patientName("Patient Test")
				.date(LocalDate.now())
				.title("Test Title")
				.content("Test Content")
				.build();

		// Save
		note =  noteRepository.save(note);
		assertNotNull(note.getId());
		assertEquals("Patient Test", note.getPatientName());

		// Update
		note.setPatientId(2L);
		note =  noteRepository.save(note);
		assertNotNull(note.getId());
		assertEquals(2L, note.getPatientId());

		mockMvc.perform(get("/api/notes/patient/" + note.getPatientId()))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Patient Test")))
				.andExpect(content().string(containsString("Test Title")))
				.andExpect(content().string(containsString("Test Content")));

		// Find
		List<Note> listResult = noteRepository.findByPatientId(2L);
		assertFalse(listResult.isEmpty());

		// Delete
		String id = note.getId();
		noteRepository.delete(note);
		Optional<Note> optionalNote = noteRepository.findById(id);
		assertFalse(optionalNote.isPresent());
	}
}
