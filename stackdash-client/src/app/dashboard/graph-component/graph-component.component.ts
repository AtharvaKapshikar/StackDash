import { CommonModule, isPlatformBrowser } from '@angular/common';
import {
  AfterViewInit,
  Component,
  ElementRef,
  ViewChild,
  inject,
  PLATFORM_ID
} from '@angular/core';

import {
  Chart,
  LineController,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  BarController,
  BarElement,
  Tooltip,
  Legend,
  Filler
} from 'chart.js';

// Register chart types and components
Chart.register(
  LineController,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  BarController,
  BarElement,
  Tooltip,
  Legend,
  Filler
);


@Component({
  selector: 'app-graph-component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './graph-component.component.html',
  styleUrl: './graph-component.component.scss'
})
export class GraphComponent implements AfterViewInit {
  @ViewChild('loginChart') loginChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('taskChart') taskChartRef!: ElementRef<HTMLCanvasElement>;

  private loginChart!: Chart;
  private taskChart!: Chart;
  private platformId = inject(PLATFORM_ID);

  ngAfterViewInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.renderLoginChart();
      this.renderTaskChart();
    }
  }

  renderLoginChart() {
    if (this.loginChart) this.loginChart.destroy();

    this.loginChart = new Chart(this.loginChartRef.nativeElement, {
      type: 'line',
      data: {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        datasets: [{
          label: 'User Logins',
          data: [12, 19, 3, 5, 2, 3, 9],
          borderColor: '#3498db',
          backgroundColor: 'rgba(52, 152, 219, 0.2)',
          fill: true,
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false }
        }
      }
    });
  }

  renderTaskChart() {
    if (this.taskChart) this.taskChart.destroy();

    this.taskChart = new Chart(this.taskChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Task A', 'Task B', 'Task C', 'Task D', 'Task E'],
        datasets: [{
          label: 'Completion %',
          data: [80, 55, 90, 40, 70],
          backgroundColor: '#2ecc71'
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false }
        },
        scales: {
          y: {
            beginAtZero: true,
            max: 100
          }
        }
      }
    });
  }

}
