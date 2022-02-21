import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('WebsiteUser e2e test', () => {
  const websiteUserPageUrl = '/website-user';
  const websiteUserPageUrlPattern = new RegExp('/website-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const websiteUserSample = { login: 'Sausages Summit', password: 'Cotton SDD' };

  let websiteUser: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/website-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/website-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/website-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (websiteUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/website-users/${websiteUser.id}`,
      }).then(() => {
        websiteUser = undefined;
      });
    }
  });

  it('WebsiteUsers menu should load WebsiteUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('website-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WebsiteUser').should('exist');
    cy.url().should('match', websiteUserPageUrlPattern);
  });

  describe('WebsiteUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(websiteUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WebsiteUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/website-user/new$'));
        cy.getEntityCreateUpdateHeading('WebsiteUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', websiteUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/website-users',
          body: websiteUserSample,
        }).then(({ body }) => {
          websiteUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/website-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/website-users?page=0&size=20>; rel="last",<http://localhost/api/website-users?page=0&size=20>; rel="first"',
              },
              body: [websiteUser],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(websiteUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WebsiteUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('websiteUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', websiteUserPageUrlPattern);
      });

      it('edit button click should load edit WebsiteUser page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WebsiteUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', websiteUserPageUrlPattern);
      });

      it('last delete button click should delete instance of WebsiteUser', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('websiteUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', websiteUserPageUrlPattern);

        websiteUser = undefined;
      });
    });
  });

  describe('new WebsiteUser page', () => {
    beforeEach(() => {
      cy.visit(`${websiteUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WebsiteUser');
    });

    it('should create an instance of WebsiteUser', () => {
      cy.get(`[data-cy="login"]`).type('Credit embrace').should('have.value', 'Credit embrace');

      cy.get(`[data-cy="firstName"]`).type('Esmeralda').should('have.value', 'Esmeralda');

      cy.get(`[data-cy="lastName"]`).type('Dooley').should('have.value', 'Dooley');

      cy.get(`[data-cy="email"]`).type('Santina_Kerluke@gmail.com').should('have.value', 'Santina_Kerluke@gmail.com');

      cy.get(`[data-cy="language"]`).type('York').should('have.value', 'York');

      cy.get(`[data-cy="profiles"]`)
        .type('Electronics withdrawal upward-trending')
        .should('have.value', 'Electronics withdrawal upward-trending');

      cy.get(`[data-cy="createdDate"]`).type('2022-02-20').should('have.value', '2022-02-20');

      cy.get(`[data-cy="modifiedDate"]`).type('2022-02-20').should('have.value', '2022-02-20');

      cy.get(`[data-cy="activated"]`).should('not.be.checked');
      cy.get(`[data-cy="activated"]`).click().should('be.checked');

      cy.get(`[data-cy="password"]`).type('Shoes platforms').should('have.value', 'Shoes platforms');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        websiteUser = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', websiteUserPageUrlPattern);
    });
  });
});
