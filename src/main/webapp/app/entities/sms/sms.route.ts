import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Sms } from 'app/shared/model/sms.model';
import { SmsService } from './sms.service';
import { SmsComponent } from './sms.component';
import { SmsDetailComponent } from './sms-detail.component';
import { SmsUpdateComponent } from './sms-update.component';
import { SmsDeletePopupComponent } from './sms-delete-dialog.component';
import { ISms } from 'app/shared/model/sms.model';

@Injectable({ providedIn: 'root' })
export class SmsResolve implements Resolve<ISms> {
    constructor(private service: SmsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((sms: HttpResponse<Sms>) => sms.body));
        }
        return of(new Sms());
    }
}

export const smsRoute: Routes = [
    {
        path: 'sms',
        component: SmsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'campaignManagerApp.sms.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sms/:id/view',
        component: SmsDetailComponent,
        resolve: {
            sms: SmsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.sms.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sms/new',
        component: SmsUpdateComponent,
        resolve: {
            sms: SmsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.sms.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sms/:id/edit',
        component: SmsUpdateComponent,
        resolve: {
            sms: SmsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.sms.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const smsPopupRoute: Routes = [
    {
        path: 'sms/:id/delete',
        component: SmsDeletePopupComponent,
        resolve: {
            sms: SmsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.sms.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
