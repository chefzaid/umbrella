import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'umbrellaApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'address',
    data: { pageTitle: 'umbrellaApp.address.home.title' },
    loadChildren: () => import('./address/address.routes'),
  },
  {
    path: 'contact',
    data: { pageTitle: 'umbrellaApp.contact.home.title' },
    loadChildren: () => import('./contact/contact.routes'),
  },
  {
    path: 'prospect',
    data: { pageTitle: 'umbrellaApp.prospect.home.title' },
    loadChildren: () => import('./prospect/prospect.routes'),
  },
  {
    path: 'app-user',
    data: { pageTitle: 'umbrellaApp.appUser.home.title' },
    loadChildren: () => import('./app-user/app-user.routes'),
  },
  {
    path: 'employee',
    data: { pageTitle: 'umbrellaApp.employee.home.title' },
    loadChildren: () => import('./employee/employee.routes'),
  },
  {
    path: 'id-document',
    data: { pageTitle: 'umbrellaApp.idDocument.home.title' },
    loadChildren: () => import('./id-document/id-document.routes'),
  },
  {
    path: 'employment-contract',
    data: { pageTitle: 'umbrellaApp.employmentContract.home.title' },
    loadChildren: () => import('./employment-contract/employment-contract.routes'),
  },
  {
    path: 'service-contract',
    data: { pageTitle: 'umbrellaApp.serviceContract.home.title' },
    loadChildren: () => import('./service-contract/service-contract.routes'),
  },
  {
    path: 'project',
    data: { pageTitle: 'umbrellaApp.project.home.title' },
    loadChildren: () => import('./project/project.routes'),
  },
  {
    path: 'client',
    data: { pageTitle: 'umbrellaApp.client.home.title' },
    loadChildren: () => import('./client/client.routes'),
  },
  {
    path: 'formula',
    data: { pageTitle: 'umbrellaApp.formula.home.title' },
    loadChildren: () => import('./formula/formula.routes'),
  },
  {
    path: 'document',
    data: { pageTitle: 'umbrellaApp.document.home.title' },
    loadChildren: () => import('./document/document.routes'),
  },
  {
    path: 'time-sheet',
    data: { pageTitle: 'umbrellaApp.timeSheet.home.title' },
    loadChildren: () => import('./time-sheet/time-sheet.routes'),
  },
  {
    path: 'time-sheet-line',
    data: { pageTitle: 'umbrellaApp.timeSheetLine.home.title' },
    loadChildren: () => import('./time-sheet-line/time-sheet-line.routes'),
  },
  {
    path: 'expense-note',
    data: { pageTitle: 'umbrellaApp.expenseNote.home.title' },
    loadChildren: () => import('./expense-note/expense-note.routes'),
  },
  {
    path: 'expense',
    data: { pageTitle: 'umbrellaApp.expense.home.title' },
    loadChildren: () => import('./expense/expense.routes'),
  },
  {
    path: 'mileage-allowance',
    data: { pageTitle: 'umbrellaApp.mileageAllowance.home.title' },
    loadChildren: () => import('./mileage-allowance/mileage-allowance.routes'),
  },
  {
    path: 'invoice',
    data: { pageTitle: 'umbrellaApp.invoice.home.title' },
    loadChildren: () => import('./invoice/invoice.routes'),
  },
  {
    path: 'invoice-line',
    data: { pageTitle: 'umbrellaApp.invoiceLine.home.title' },
    loadChildren: () => import('./invoice-line/invoice-line.routes'),
  },
  {
    path: 'wallet',
    data: { pageTitle: 'umbrellaApp.wallet.home.title' },
    loadChildren: () => import('./wallet/wallet.routes'),
  },
  {
    path: 'activity-report',
    data: { pageTitle: 'umbrellaApp.activityReport.home.title' },
    loadChildren: () => import('./activity-report/activity-report.routes'),
  },
  {
    path: 'operation',
    data: { pageTitle: 'umbrellaApp.operation.home.title' },
    loadChildren: () => import('./operation/operation.routes'),
  },
  {
    path: 'pay-slip',
    data: { pageTitle: 'umbrellaApp.paySlip.home.title' },
    loadChildren: () => import('./pay-slip/pay-slip.routes'),
  },
  {
    path: 'enterprise',
    data: { pageTitle: 'umbrellaApp.enterprise.home.title' },
    loadChildren: () => import('./enterprise/enterprise.routes'),
  },
  {
    path: 'parameter',
    data: { pageTitle: 'umbrellaApp.parameter.home.title' },
    loadChildren: () => import('./parameter/parameter.routes'),
  },
  {
    path: 'parameter-group',
    data: { pageTitle: 'umbrellaApp.parameterGroup.home.title' },
    loadChildren: () => import('./parameter-group/parameter-group.routes'),
  },
  {
    path: 'expense-type',
    data: { pageTitle: 'umbrellaApp.expenseType.home.title' },
    loadChildren: () => import('./expense-type/expense-type.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'umbrellaApp.notification.home.title' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
