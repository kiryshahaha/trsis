package ru.spbguap.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedUserShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnAllBooksWhenAuthenticated() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void userCannotCreateBookForbidden() throws Exception {
        String json = """
                {
                  "title": "Запрещённая книга",
                  "author": "Автор",
                  "genre": "Жанр",
                  "year": 2024,
                  "price": 100.0,
                  "stock": 1
                }
                """;
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanCreateBook() throws Exception {
        String json = """
                {
                  "title": "Тест книга",
                  "author": "Тест автор",
                  "genre": "Тест жанр",
                  "year": 2024,
                  "price": 100.0,
                  "stock": 1
                }
                """;
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Тест книга"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnBookById() throws Exception {
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturn404ForMissingBook() throws Exception {
        mockMvc.perform(get("/api/books/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanUpdateBook() throws Exception {
        String json = """
                {
                  "title": "Обновлённое название",
                  "author": "Новый автор",
                  "genre": "Новый жанр",
                  "year": 2025,
                  "price": 999.0,
                  "stock": 2
                }
                """;
        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Обновлённое название"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanDeleteBook() throws Exception {
        String json = """
                {"title":"Удалить меня","author":"А","genre":"Б","year":2000,"price":1.0,"stock":1}
                """;
        String response = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/books/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/books/" + id))
                .andExpect(status().isNotFound());
    }
}
