import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OtsComponent } from './ots-component';

describe('OtsComponent', () => {
  let component: OtsComponent;
  let fixture: ComponentFixture<OtsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OtsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OtsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
