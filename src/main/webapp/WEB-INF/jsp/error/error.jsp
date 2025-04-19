<jsp:include page="../common/head.jsp" />
<body>
    <div class="flex items-center justify-center h-screen bg-gray-100">
        <div class="bg-white p-6 rounded-lg shadow-lg text-center">
            <h1 class="text-4xl font-bold text-red-600">${errorDetails.status} Error</h1>
            <p class="mt-4 text-gray-700">An error occurred while processing your request.</p>
            <p>${errorDetails.message}</p>
            <a href="${pageContext.request.contextPath}/user" class="mt-6 inline-block px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Go to Home</a>
        </div>
    </div>
</body>
