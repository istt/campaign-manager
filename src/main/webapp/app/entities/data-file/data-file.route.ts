import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DataFile } from 'app/shared/model/data-file.model';
import { DataFileService } from './data-file.service';
import { DataFileComponent } from './data-file.component';
import { DataFileDetailComponent } from './data-file-detail.component';
import { DataFileUpdateComponent } from './data-file-update.component';
import { DataFileDeletePopupComponent } from './data-file-delete-dialog.component';
import { IDataFile } from 'app/shared/model/data-file.model';

@Injectable({ providedIn: 'root' })
export class DataFileResolve implements Resolve<IDataFile> {
    constructor(private service: DataFileService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((dataFile: HttpResponse<DataFile>) => dataFile.body));
        }
        return of(new DataFile());
    }
}

export const dataFileRoute: Routes = [
    {
        path: 'data-file',
        component: DataFileComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'campaignManagerApp.dataFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'data-file/:id/view',
        component: DataFileDetailComponent,
        resolve: {
            dataFile: DataFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.dataFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'data-file/new',
        component: DataFileUpdateComponent,
        resolve: {
            dataFile: DataFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.dataFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'data-file/:id/edit',
        component: DataFileUpdateComponent,
        resolve: {
            dataFile: DataFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.dataFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dataFilePopupRoute: Routes = [
    {
        path: 'data-file/:id/delete',
        component: DataFileDeletePopupComponent,
        resolve: {
            dataFile: DataFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'campaignManagerApp.dataFile.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
