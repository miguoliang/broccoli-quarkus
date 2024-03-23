module.exports = {
  broccoli: {
    input: "openapi3.yaml",
    output: {
      target: "src/api.ts",
      baseUrl: "/api",
      client: "react-query"
    }
  }
};