import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WebsiteUserDetailComponent } from './website-user-detail.component';

describe('WebsiteUser Management Detail Component', () => {
  let comp: WebsiteUserDetailComponent;
  let fixture: ComponentFixture<WebsiteUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WebsiteUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ websiteUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WebsiteUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WebsiteUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load websiteUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.websiteUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
