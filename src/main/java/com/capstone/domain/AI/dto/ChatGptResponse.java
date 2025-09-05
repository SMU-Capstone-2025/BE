package com.capstone.domain.AI.dto;


import java.util.List;

public record ChatGptResponse(
	List<Choice> choices
) {
	public record Choice(
		Message message
	) {}

	public record Message(
		String role,
		String content
	) {}
}
