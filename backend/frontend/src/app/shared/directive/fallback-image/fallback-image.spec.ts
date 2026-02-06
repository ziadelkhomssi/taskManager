import { ElementRef, Renderer2, RendererFactory2 } from '@angular/core';
import { FallbackImage } from './fallback-image';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FallbackImageTestHost } from './fallback-image-test-host';

describe('FallbackImage Directive', () => {
  let fixture: ComponentFixture<FallbackImageTestHost>;
  let img: HTMLImageElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FallbackImage, FallbackImageTestHost],
    });

    fixture = TestBed.createComponent(FallbackImageTestHost);
    fixture.detectChanges();

    img = fixture.nativeElement.querySelector('img');
  });

  it('should replace src with fallback on error', () => {
    img.dispatchEvent(new Event('error'));
    fixture.detectChanges();

    expect(img.src).toContain('assets/images/gray-square.png');
  });
});

