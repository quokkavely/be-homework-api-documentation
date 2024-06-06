package com.springboot.slice.controller.member;

import com.springboot.member.dto.MemberDto;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller의 API만 이용하는 방법(리팩토링 전)
 */
@Transactional     // 테스트 케이스 하나의 실행이 끝나면 매 번 rollback 처리를 해준다.
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerV1Test {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Test
    public void postMemberTest() throws Exception {
        /** 중복 코드 시작 */
        // given: MemberController의 postMember()를 테스트 하기 위한 테스트 데이터를 미리 생성
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com",
                "홍길동",
                "010-1234-5678");
        String content = gson.toJson(post);
        URI uri = UriComponentsBuilder.newInstance().path("/v11/members").build().toUri();

        // when
        ResultActions actions =
                mockMvc.perform(
                            post(uri)
                                    .accept(MediaType.APPLICATION_JSON)   /** 중복 */
                                    .contentType(MediaType.APPLICATION_JSON)   /** 중복 */
                                    .content(content)   /** 중복 */
                );
        /** 중복 코드 끝 */

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/v11/members/"))));
    }

    @Test
    void patchMemberTest() throws Exception {
        /** 중복 코드 시작 */
        // given
        // MemberController의 patchMember()를 테스트하기 위해 postMember()를 이용해 테스트 데이터를 생성 후, DB에 저장
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com","홍길동","010-1111-1111");
        String postContent = gson.toJson(post);
        URI postUri = UriComponentsBuilder.newInstance().path("/v11/members").build().toUri();

        ResultActions postActions =
                mockMvc.perform(
                        post(postUri)
                                .accept(MediaType.APPLICATION_JSON)    /** 중복 */
                                .contentType(MediaType.APPLICATION_JSON)  /** 중복 */
                                .content(postContent)
                );

        String location = postActions.andReturn().getResponse().getHeader("Location"); // "/v11/members/1"
        /** 중복 코드 끝 */

        // MemberController의 patchMember()를 테스트하기 위한 테스트 데이터를 생성 후, DB에 업데이트
        MemberDto.Patch patch = new MemberDto.Patch(1, null, "010-2222-2222", null);

        String patchContent = gson.toJson(patch);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(location)
                        .accept(MediaType.APPLICATION_JSON)      /** 중복 */
                        .contentType(MediaType.APPLICATION_JSON) /** 중복 */
                        .content(patchContent)   /** 중복 */
                );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.phone").value(patch.getPhone()));
    }

    @Test
    void getMemberTest() throws Exception {
        /** 중복 코드 시작 */
        // given: MemberController의 getMember()를 테스트하기 위해 postMember()를 이용해 테스트 데이터를 생성 후, DB에 저장
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com","홍길동","010-1111-1111");
        String postContent = gson.toJson(post);
        URI postUri = UriComponentsBuilder.newInstance().path("/v11/members").build().toUri();

        ResultActions postActions =
                mockMvc.perform(
                        post(postUri)
                                .accept(MediaType.APPLICATION_JSON)    /** 중복 */
                                .contentType(MediaType.APPLICATION_JSON)  /** 중복 */
                                .content(postContent)     /** 중복 */
                );
        String location = postActions.andReturn().getResponse().getHeader("Location"); // "/v11/members/1"
        /** 중복 코드 끝 */


        // when / then
        mockMvc.perform(
                    get(location)
                            .accept(MediaType.APPLICATION_JSON)   /** 중복 */
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(post.getEmail()))
                .andExpect(jsonPath("$.data.name").value(post.getName()))
                .andExpect(jsonPath("$.data.phone").value(post.getPhone()));
    }

    @Test
    void getMembersTest() throws Exception {
        /** 중복 코드 시작 */
        // given: MemberController의 getMembers()를 테스트하기 위해 postMember()를 이용해 테스트 데이터(2건)를 생성 후, DB에 저장
        MemberDto.Post post1 = new MemberDto.Post("hgd1@gmail.com","홍길동1","010-1111-1111");
        String postContent1 = gson.toJson(post1);
        URI postUri = UriComponentsBuilder.newInstance().path("/v11/members").build().toUri();

        mockMvc.perform(
                    post(postUri)
                            .accept(MediaType.APPLICATION_JSON)    /** 중복 */
                            .contentType(MediaType.APPLICATION_JSON)  /** 중복 */
                            .content(postContent1)   /** 중복 */
                );

        MemberDto.Post post2 = new MemberDto.Post("hgd2@gmail.com","홍길동2","010-2222-2222");
        String postContent2 = gson.toJson(post2);

        mockMvc.perform(
                    post(postUri)
                            .accept(MediaType.APPLICATION_JSON)    /** 중복 */
                            .contentType(MediaType.APPLICATION_JSON)  /** 중복 */
                            .content(postContent2)   /** 중복 */
                );
        /** 중복 코드 끝 */

        String page = "1";
        String size = "10";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", page);
        queryParams.add("size", size);

        /** 중복 */
        URI getUri = UriComponentsBuilder.newInstance().path("/v11/members").build().toUri();

        // when
        ResultActions actions =
                mockMvc.perform(
                            get(getUri)
                                    .params(queryParams)
                                    .accept(MediaType.APPLICATION_JSON)   /** 중복 */
                );

        // then
        MvcResult result = actions
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data").isArray())
                                .andReturn();

        List list = JsonPath.parse(result.getResponse().getContentAsString()).read("$.data");

        assertThat(list.size(), is(2));

//        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void deleteMemberTest() throws Exception {
        /** 중복 코드 시작 */
        // given: MemberController의 deleteMember()를 테스트하기 위해 postMember()를 이용해 테스트 데이터를 생성 후, DB에 저장
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com","홍길동","010-1111-1111");
        String postContent = gson.toJson(post);
        URI postUri = UriComponentsBuilder.newInstance().path("/v11/members").build().toUri();

        ResultActions postActions =
                mockMvc.perform(
                        post(postUri)
                                .accept(MediaType.APPLICATION_JSON)    /** 중복 */
                                .contentType(MediaType.APPLICATION_JSON)  /** 중복 */
                                .content(postContent)
                );

        String location = postActions.andReturn().getResponse().getHeader("Location"); // "/v11/members/1"
        /** 중복 코드 끝 */

        // when / then
        mockMvc.perform(delete(location))
                .andExpect(status().isNoContent());
    }
}
