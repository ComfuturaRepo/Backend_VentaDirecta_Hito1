import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NgselectDropdownComponent } from './ngselect-dropdown-component';

describe('NgselectDropdownComponent', () => {
  let component: NgselectDropdownComponent;
  let fixture: ComponentFixture<NgselectDropdownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NgselectDropdownComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NgselectDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
