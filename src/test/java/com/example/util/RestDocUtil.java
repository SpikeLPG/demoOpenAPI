package com.example.util;

import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcOperationPreprocessorsConfigurer;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class RestDocUtil {

    public static MockMvcOperationPreprocessorsConfigurer restDocSetup(RestDocumentationContextProvider restDocumentation) {
        return documentationConfiguration(restDocumentation).operationPreprocessors()
                .withRequestDefaults(modifyHeaders().remove("Host"), prettyPrint())
                .withResponseDefaults(prettyPrint());
    }

    public static RequestFieldsSnippet requestAddSongDescriptor = requestFields(
            fieldWithPath("title").description("Name of the song"),
            fieldWithPath("composer").description("Name of the composer"),
            fieldWithPath("performers").description("List of performers").optional().type(JsonFieldType.ARRAY),
            fieldWithPath("performers[].performerName").description("Name of the performer").optional().type(JsonFieldType.STRING),
            fieldWithPath("performers[].dateTime").description("Date and Time of the performance (UTC)").optional().type(JsonFieldType.STRING)
    );

    // If a field is optional then it must have a type associated as RestDoc is unable to automatically assign it a type
    public static ResponseFieldsSnippet responseSongListDescriptor = responseFields(
            fieldWithPath("[].id").description("Id of the song").optional().type(JsonFieldType.NUMBER),
            fieldWithPath("[].title").description("Name of the song").optional().type(JsonFieldType.STRING),
            fieldWithPath("[].composer").description("Name of the composer").optional().type(JsonFieldType.STRING),
            fieldWithPath("[].performers").description("List of performers").optional().type(JsonFieldType.ARRAY),
            fieldWithPath("[].performers[].performerName").description("Name of the performer").optional().type(JsonFieldType.STRING),
            fieldWithPath("[].performers[].dateTime").description("Date and Time of the performance (UTC)").optional().type(JsonFieldType.STRING)
    );

    public static ResponseFieldsSnippet responseSongDescriptor = responseFields(
            fieldWithPath("id").description("Id of the song"),
            fieldWithPath("title").description("Name of the song"),
            fieldWithPath("composer").description("Name of the composer"),
            fieldWithPath("performers").description("List of performers"),
            fieldWithPath("performers[].performerName").description("Name of the performer").optional().type(JsonFieldType.STRING),
            fieldWithPath("performers[].dateTime").description("Date and Time of the performance (UTC)").optional().type(JsonFieldType.STRING)
    );

    public static ResponseFieldsSnippet responseErrorDescriptor = responseFields(
            fieldWithPath("errorCode").description("Error code for the error"),
            fieldWithPath("message").description("Associated error moessage")
    );
}
