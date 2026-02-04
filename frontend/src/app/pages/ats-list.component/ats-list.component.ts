import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import { ATSListDTO } from '../../model/ssoma.model';

@Component({
  selector: 'app-ats-list',
  standalone: true,
  imports: [CommonModule, RouterModule, PaginationComponent],
  templateUrl: './ats-list.component.html',
  styleUrls: ['./ats-list.component.css']
})
export class ATSListComponent implements OnInit {
  @Input() atsList: ATSListDTO[] = [];
  @Input() currentPage: number = 0;
  @Input() pageSize: number = 10;
  @Input() totalItems: number = 0;
  @Input() totalPages: number = 0;
  @Input() isLoading: boolean = false;
  @Input() searchTerm: string = '';

  @Output() pageChange = new EventEmitter<number>();
  @Output() pageSizeChange = new EventEmitter<number>();
  @Output() viewDetails = new EventEmitter<number>();
  @Output() refreshList = new EventEmitter<void>();
  @Output() search = new EventEmitter<string>();

  ngOnInit(): void {}

  onPageChange(page: number): void {
    this.pageChange.emit(page);
  }

  onPageSizeChange(size: number): void {
    this.pageSizeChange.emit(size);
  }

  onRefresh(): void {
    this.refreshList.emit();
  }

  onSearch(): void {
    this.search.emit(this.searchTerm);
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.search.emit('');
  }

  getShowingFrom(): number {
    if (this.totalItems === 0) return 0;
    return (this.currentPage * this.pageSize) + 1;
  }

  getShowingTo(): number {
    if (this.totalItems === 0) return 0;
    return Math.min((this.currentPage + 1) * this.pageSize, this.totalItems);
  }
}
