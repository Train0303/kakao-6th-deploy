package com.example.kakao.user;

import com.example.kakao.MyRestDoc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8080)
@Sql(value = "classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserRestControllerTest extends MyRestDoc {
    private final ObjectMapper om;

    @Autowired
    public UserRestControllerTest(ObjectMapper om) {
        this.om = om;
    }

    /**
     * 회원가입 테스트
     */

    @DisplayName("유저_회원가입_테스트")
    @Test
    public void user_join_test() throws Exception{
        // given
        UserRequest.JoinDTO userRequest = new UserRequest.JoinDTO();
        userRequest.setUsername("rjsdnxogh12");
        userRequest.setEmail("rjsdnxogh12@naver.com");
        userRequest.setPassword("test1234!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("true"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error").value(nullValue()));

        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_회원가입_테스트_실패_이미_존재하는_이메일")
    @Test
    public void user_join_test_email_already_exist() throws Exception{
        // given
        UserRequest.JoinDTO userRequest = new UserRequest.JoinDTO();
        userRequest.setUsername("ssarmango");
        userRequest.setEmail("ssarmango@nate.com");
        userRequest.setPassword("test1234!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("동일한 이메일이 존재합니다 : " + "ssarmango@nate.com"),
                jsonPath("$.error.status").value(400)
                );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_회원가입_테스트_실패_이메일_형식_미충족")
    @Test
    public void user_join_test_not_satisfied_email_expression() throws Exception{
        // given
        UserRequest.JoinDTO userRequest = new UserRequest.JoinDTO();
        userRequest.setUsername("rjsdnxogh12");
        userRequest.setEmail("rjsdnxogh12naver.com");
        userRequest.setPassword("test1234!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("이메일 형식으로 작성해야 합니다.:email"),
                jsonPath("$.error.status").value(400)
        );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_회원가입_테스트_실패_너무_짧은_유저이름")
    @Test
    public void user_join_test_too_short_username() throws Exception{
        // given
        UserRequest.JoinDTO userRequest = new UserRequest.JoinDTO();
        userRequest.setUsername("rjsdn");
        userRequest.setEmail("rjsdnxogh12naver.com");
        userRequest.setPassword("test1234!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("8에서 45자 이내여야 합니다.:username"),
                jsonPath("$.error.status").value(400)
        );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_회원가입_테스트_실패_너무_긴_유저이름")
    @Test
    public void user_join_test_too_long_username() throws Exception{
        // given
        UserRequest.JoinDTO userRequest = new UserRequest.JoinDTO();
        userRequest.setUsername("rjsdnxogh12rjsdnxogh12rjsdnxogh12rjsdnxogh12rjsdnxogh12rjsdnxogh12rjsdnxogh12rjsdnxogh12");
        userRequest.setEmail("rjsdnxogh12naver.com");
        userRequest.setPassword("test1234!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("8에서 45자 이내여야 합니다.:username"),
                jsonPath("$.error.status").value(400)
        );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_회원가입_테스트_실패_너무_짧은_비밀번호")
    @Test
    public void user_join_test_too_short_password() throws Exception{
        // given
        UserRequest.JoinDTO userRequest = new UserRequest.JoinDTO();
        userRequest.setUsername("rjsdnxogh12");
        userRequest.setEmail("rjsdnxogh12@naver.com");
        userRequest.setPassword("te12!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("8에서 20자 이내여야 합니다.:password"),
                jsonPath("$.error.status").value(400)
        );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_회원가입_테스트_실패_너무_긴_비밀번호")
    @Test
    public void user_join_test_too_long_password() throws Exception{
        // given
        UserRequest.JoinDTO userRequest = new UserRequest.JoinDTO();
        userRequest.setUsername("rjsdnxogh12");
        userRequest.setEmail("rjsdnxogh12@naver.com");
        userRequest.setPassword("test12!test12!test12!test12!test12!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("8에서 20자 이내여야 합니다.:password"),
                jsonPath("$.error.status").value(400)
        );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_회원가입_테스트_실패_비밀번호_형식_미충족")
    @Test
    public void user_join_test_not_satisfied_password_expression() throws Exception{
        // given
        UserRequest.JoinDTO userRequest = new UserRequest.JoinDTO();
        userRequest.setUsername("rjsdnxogh12");
        userRequest.setEmail("rjsdnxogh12@naver.com");
        userRequest.setPassword("123141 352");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.:password"),
                jsonPath("$.error.status").value(400)
        );
        result.andDo(print()).andDo(document);
    }

    /**
     * 로그인 테스트
     */

    @DisplayName("유저_로그인_테스트")
    @Test
    public void user_login_test() throws Exception{
        // given
        UserRequest.LoginDTO userRequest = new UserRequest.LoginDTO();
        userRequest.setEmail("ssarmango@nate.com");
        userRequest.setPassword("meta1234!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        String header = result.andReturn().getResponse().getHeader("Authorization");
        System.out.println("테스트 : " + response);

        // then
        assertTrue(header.startsWith("Bearer "));
        result.andExpectAll(
                jsonPath("$.success").value("true"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error").value(nullValue())
        );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_로그인_테스트_실패_없는_회원")
    @Test
    public void user_login_test_fail_user_not_found() throws Exception{
        // given
        UserRequest.LoginDTO userRequest = new UserRequest.LoginDTO();
        String email = "ssarmangomango@nate.com";
        userRequest.setEmail(email);
        userRequest.setPassword("meta1234!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("이메일을 찾을 수 없습니다 : " + email),
                jsonPath("$.error.status").value(404)
        );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_로그인_테스트_실패_틀린_비밀번호")
    @Test
    public void user_login_test_fail_wrong_password() throws Exception{
        // given
        UserRequest.LoginDTO userRequest = new UserRequest.LoginDTO();
        String email = "ssarmango@nate.com";
        userRequest.setEmail(email);
        userRequest.setPassword("meta1234!@");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("비밀번호가 일치하지 않습니다."),
                jsonPath("$.error.status").value(400)
        );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_로그인_테스트_실패_이메일_형식_미충족")
    @Test
    public void user_login_test_fail_not_satisfied_email_expression() throws Exception{
        // given
        UserRequest.LoginDTO userRequest = new UserRequest.LoginDTO();
        String email = "ssarmangonate.com";
        userRequest.setEmail(email);
        userRequest.setPassword("meta1234!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("이메일 형식으로 작성해야 합니다.:email"),
                jsonPath("$.error.status").value(400)
        );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_로그인_테스트_실패_너무_짧은_비밀번호")
    @Test
    public void user_login_test_fail_too_short_password() throws Exception{
        // given
        UserRequest.LoginDTO userRequest = new UserRequest.LoginDTO();
        String email = "ssarmango@nate.com";
        userRequest.setEmail(email);
        userRequest.setPassword("meta1!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("8에서 20자 이내여야 합니다.:password"),
                jsonPath("$.error.status").value(400)
        );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_로그인_테스트_실패_너무_긴_비밀번호")
    @Test
    public void user_login_test_fail_too_long_password() throws Exception{
        // given
        UserRequest.LoginDTO userRequest = new UserRequest.LoginDTO();
        String email = "ssarmango@nate.com";
        userRequest.setEmail(email);
        userRequest.setPassword("meta12!meta12!meta12!meta12!meta12!meta12!meta12!meta12!meta12!");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("8에서 20자 이내여야 합니다.:password"),
                jsonPath("$.error.status").value(400)
        );
        result.andDo(print()).andDo(document);
    }

    @DisplayName("유저_로그인_테스트_실패_비밀번호_형식_미충족")
    @Test
    public void user_login_test_fail_not_satisfied_password_expression() throws Exception{
        // given
        UserRequest.LoginDTO userRequest = new UserRequest.LoginDTO();
        String email = "ssarmango@nate.com";
        userRequest.setEmail(email);
        userRequest.setPassword("ewreqr1 2121");
        String requestBody = om.writeValueAsString(userRequest);

        // when
        ResultActions result = mvc.perform(
                post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + response);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("false"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error.message").value("영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.:password"),
                jsonPath("$.error.status").value(400)
        );
        result.andDo(print()).andDo(document);
    }

    /**
     * 이메일 체크 테스트
     */

    @DisplayName("이메일_검사_테스트")
    @Test
    public void same_email_check_test() throws Exception {
        // given
        String email = "rjsdnxogh12@naver.com";
        UserRequest.EmailCheckDTO emailCheckDTO = new UserRequest.EmailCheckDTO();
        emailCheckDTO.setEmail(email);
        String requestBody = om.writeValueAsString(emailCheckDTO);

        // when
        ResultActions result = mvc.perform(
                post("/check")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("true"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error").value(nullValue())
        );
    }

    @DisplayName("이메일_검사_테스트_실패_이미_존재하는_이메일")
    @Test
    public void same_email_test_fail_already_exist_email() throws Exception {
        // given
        String email = "rjsdnxogh12@naver.com";
        UserRequest.EmailCheckDTO emailCheckDTO = new UserRequest.EmailCheckDTO();
        emailCheckDTO.setEmail(email);
        String requestBody = om.writeValueAsString(emailCheckDTO);

        // when
        ResultActions result = mvc.perform(
                post("/check")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // eye
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // then
        result.andExpectAll(
                jsonPath("$.success").value("true"),
                jsonPath("$.response").value(nullValue()),
                jsonPath("$.error").value(nullValue())
        );
    }
}
