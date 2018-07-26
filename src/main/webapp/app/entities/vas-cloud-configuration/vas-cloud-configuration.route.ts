import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { VasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';
import { VasCloudConfigurationService } from './vas-cloud-configuration.service';
import { VasCloudConfigurationComponent } from './vas-cloud-configuration.component';
import { VasCloudConfigurationDetailComponent } from './vas-cloud-configuration-detail.component';
import { VasCloudConfigurationUpdateComponent } from './vas-cloud-configuration-update.component';
import { VasCloudConfigurationDeletePopupComponent } from './vas-cloud-configuration-delete-dialog.component';
import { IVasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';

@Injectable({ providedIn: 'root' })
export class VasCloudConfigurationResolve implements Resolve<IVasCloudConfiguration> {
    constructor(private service: VasCloudConfigurationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((vasCloudConfiguration: HttpResponse<VasCloudConfiguration>) => vasCloudConfiguration.body));
        }
        return of(new VasCloudConfiguration());
    }
}

export const vasCloudConfigurationRoute: Routes = [
    {
        path: 'vas-cloud-configuration',
        component: VasCloudConfigurationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.vasCloudConfiguration.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'vas-cloud-configuration/:id/view',
        component: VasCloudConfigurationDetailComponent,
        resolve: {
            vasCloudConfiguration: VasCloudConfigurationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.vasCloudConfiguration.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'vas-cloud-configuration/new',
        component: VasCloudConfigurationUpdateComponent,
        resolve: {
            vasCloudConfiguration: VasCloudConfigurationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.vasCloudConfiguration.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'vas-cloud-configuration/:id/edit',
        component: VasCloudConfigurationUpdateComponent,
        resolve: {
            vasCloudConfiguration: VasCloudConfigurationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.vasCloudConfiguration.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const vasCloudConfigurationPopupRoute: Routes = [
    {
        path: 'vas-cloud-configuration/:id/delete',
        component: VasCloudConfigurationDeletePopupComponent,
        resolve: {
            vasCloudConfiguration: VasCloudConfigurationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.vasCloudConfiguration.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
