import { Component } from "@angular/core";

@Component({
  template: `
    <img
      [src]="src"
    />
  `
})
export class FallbackImageTestHost {
  src = 'invalid-image.png';
}
