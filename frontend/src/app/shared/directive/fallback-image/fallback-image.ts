import { Directive, ElementRef, HostListener, Input, Renderer2 } from '@angular/core';

@Directive({
  selector: 'img[src]'
})
export class FallbackImage {
  private fallbackImage = 'assets/images/gray-square.png';

  constructor(
    private elementRef: ElementRef<HTMLImageElement>,
    private renderer: Renderer2
  ) {}

  @HostListener('error')
  onError() {
    this.renderer.setAttribute(
      this.elementRef.nativeElement,
      'src',
      this.fallbackImage
    );
  }

  ngOnChanges() {
    const img = this.elementRef.nativeElement;
    const src = img.getAttribute('src');

    if (src) {
      this.renderer.removeAttribute(img, 'src');
      this.renderer.setAttribute(img, 'src', src);
    }
  }
}
