package com.woowa.woowakit.restDocsHelper;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;

public class RestDocsHelper {

	public static RestDocumentationResultHandler defaultDocument(
		final String path,
		final RequestFields requestFields,
		final ResponseFields responseFields
	) {
		return document(path,
			preprocessRequest(Preprocessors.prettyPrint()),
			preprocessResponse(Preprocessors.prettyPrint()),
			requestFields(
				requestFieldsDescriptor(requestFields.getValues())),
			responseFields(
				responseFieldsDescriptor(responseFields.getValues()))
		);
	}

	public static RestDocumentationResultHandler defaultDocument(
		final String path,
		final ResponseFields responseFields
	) {
		return document(path,
			preprocessRequest(Preprocessors.prettyPrint()),
			preprocessResponse(Preprocessors.prettyPrint()),
			responseFields(
				responseFieldsDescriptor(responseFields.getValues()))
		);
	}

	public static RestDocumentationResultHandler defaultDocument(
		final String path,
		final PathParam pathParam,
		final ResponseFields responseFields
	) {
		return document(path,
			preprocessRequest(Preprocessors.prettyPrint()),
			preprocessResponse(Preprocessors.prettyPrint()),
			pathParameters(
				parameterWithName(pathParam.getName()).description(pathParam.getDescription())),
			responseFields(
				responseFieldsDescriptor(responseFields.getValues()))
		);
	}

	public static RestDocumentationResultHandler authorizationDocument(
		final String path,
		final ResponseFields responseFields
	) {
		return document(path,
			preprocessRequest(Preprocessors.prettyPrint()),
			preprocessResponse(Preprocessors.prettyPrint()),
			requestHeaders(
				headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")),
			responseFields(
				responseFieldsDescriptor(responseFields.getValues()))
		);
	}

	public static RestDocumentationResultHandler authorizationDocument(
		final String path,
		final RequestFields requestFields
	) {
		return document(path,
			preprocessRequest(Preprocessors.prettyPrint()),
			preprocessResponse(Preprocessors.prettyPrint()),
			requestHeaders(
				headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")),
			requestFields(
				requestFieldsDescriptor(requestFields.getValues()))
		);
	}

	public static RestDocumentationResultHandler authorizationDocument(
		final String path,
		final PathParam pathParam

	) {
		return document(path,
			preprocessRequest(Preprocessors.prettyPrint()),
			preprocessResponse(Preprocessors.prettyPrint()),
			requestHeaders(
				headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")),
			pathParameters(
				parameterWithName(pathParam.getName()).description(pathParam.getDescription()))
		);
	}

	public static RestDocumentationResultHandler authorizationDocument(
		final String path,
		final RequestFields requestFields,
		final ResponseFields responseFields

	) {
		return document(path,
			preprocessRequest(Preprocessors.prettyPrint()),
			preprocessResponse(Preprocessors.prettyPrint()),
			requestHeaders(
				headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")),
			requestFields(
				requestFieldsDescriptor(requestFields.getValues())),
			responseFields(
				responseFieldsDescriptor(responseFields.getValues()))

		);
	}

	public static RestDocumentationResultHandler authorizationDocument(
		final String path,
		final PathParam pathParam,
		final RequestFields requestFields

	) {
		return document(path,
			preprocessRequest(Preprocessors.prettyPrint()),
			preprocessResponse(Preprocessors.prettyPrint()),
			requestHeaders(
				headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")),
			pathParameters(
				parameterWithName(pathParam.getName()).description(pathParam.getDescription())),
			requestFields(
				requestFieldsDescriptor(requestFields.getValues()))
		);
	}

	public static RestDocumentationResultHandler authorizationDocument(
		final String path,
		final PathParam pathParam,
		final ResponseFields responseFields

	) {
		return document(path,
			preprocessRequest(Preprocessors.prettyPrint()),
			preprocessResponse(Preprocessors.prettyPrint()),
			requestHeaders(
				headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")),
			pathParameters(
				parameterWithName(pathParam.getName()).description(pathParam.getDescription())),
			responseFields(
				responseFieldsDescriptor(responseFields.getValues()))
		);
	}

	private static List<FieldDescriptor> requestFieldsDescriptor(final Map<String, String> keyValue) {
		List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
		for (String key : keyValue.keySet()) {
			fieldDescriptors.add(fieldWithPath(key).description(keyValue.get(key)));
		}
		return fieldDescriptors;
	}

	private static List<FieldDescriptor> responseFieldsDescriptor(final Map<String, String> keyValue) {
		List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
		for (String key : keyValue.keySet()) {
			fieldDescriptors.add(fieldWithPath(key).description(keyValue.get(key)));
		}
		return fieldDescriptors;
	}

	private static RestDocumentationResultHandler getRestDocumentationResultHandler(final String path) {
		return document(path,
			preprocessRequest(Preprocessors.prettyPrint()),
			preprocessResponse(Preprocessors.prettyPrint()));
	}
}
