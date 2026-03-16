import {Routes} from '@angular/router';
import {CalculatorComponent} from '@components/calculator-component/calculator-component';

export const routes: Routes = [
  {path: '', component: CalculatorComponent},
  {path: '**', redirectTo: ''}
];
