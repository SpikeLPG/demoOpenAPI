package com.example.error;

public record ServiceError(String errorCode, String message) {
    //language=JSON
    public static final String EXAMPLE_SERVICE_UNAVAILABLE = """
            {
                "errorCode": "SERVICE-001",
                "message": "We were unable to process your request, please try again."
            }
            """;
    public static final ServiceError SERVICE_UNAVAILABLE = new ServiceError("SERVICE-001", "We were unable to process your request, please try again.");
}
