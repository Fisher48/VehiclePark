describe("Login on a website", () => {

	it("Should visit the localhost page", () => {
		cy.visit("http://localhost:8888/auth/login");
		cy.get("#username").type("Ivan");
		cy.get("#password").type("12345");
		cy.get("button[type='submit']").click();
	});

});