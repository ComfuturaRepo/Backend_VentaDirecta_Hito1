import { Component, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent {
  @Input() placeholder: string = 'Buscar...';
  @Input() buttonText: string = 'Buscar';
  @Input() showClearButton: boolean = true;
  @Input() searchTerm: string = '';

  @Output() search = new EventEmitter<string>();
  @Output() clear = new EventEmitter<void>();

  onSearch(): void {
    this.search.emit(this.searchTerm);
  }

  onClear(): void {
    this.searchTerm = '';
    this.clear.emit();
    this.search.emit('');
  }

  onKeyUp(event: KeyboardEvent): void {
    if (event.key === 'Enter') {
      this.onSearch();
    }
  }
}
