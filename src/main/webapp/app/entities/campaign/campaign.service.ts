import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICampaign } from 'app/shared/model/campaign.model';

type EntityResponseType = HttpResponse<ICampaign>;
type EntityArrayResponseType = HttpResponse<ICampaign[]>;

@Injectable({ providedIn: 'root' })
export class CampaignService {
    private resourceUrl = SERVER_API_URL + 'api/campaigns';
    public stateOptions = [-9, 0, 1, 9];

    constructor(private http: HttpClient) {}

    create(campaign: ICampaign): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(campaign);
        return this.http
            .post<ICampaign>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(campaign: ICampaign): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(campaign);
        return this.http
            .put<ICampaign>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ICampaign>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICampaign[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    /* Toggle update */
    changeState(id, state): Observable<EntityResponseType> {
        return this.http
            .put<ICampaign>(`${this.resourceUrl}/${id}`, { state }, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    private convertDateFromClient(campaign: ICampaign): ICampaign {
        const copy: ICampaign = Object.assign({}, campaign, {
            createdAt: campaign.createdAt != null && campaign.createdAt.isValid() ? campaign.createdAt.toJSON() : null,
            approvedAt: campaign.approvedAt != null && campaign.approvedAt.isValid() ? campaign.approvedAt.toJSON() : null,
            startAt: campaign.startAt != null && campaign.startAt.isValid() ? campaign.startAt.toJSON() : null,
            expiredAt: campaign.expiredAt != null && campaign.expiredAt.isValid() ? campaign.expiredAt.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
        res.body.approvedAt = res.body.approvedAt != null ? moment(res.body.approvedAt) : null;
        res.body.startAt = res.body.startAt != null ? moment(res.body.startAt) : null;
        res.body.expiredAt = res.body.expiredAt != null ? moment(res.body.expiredAt) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((campaign: ICampaign) => {
            campaign.createdAt = campaign.createdAt != null ? moment(campaign.createdAt) : null;
            campaign.approvedAt = campaign.approvedAt != null ? moment(campaign.approvedAt) : null;
            campaign.startAt = campaign.startAt != null ? moment(campaign.startAt) : null;
            campaign.expiredAt = campaign.expiredAt != null ? moment(campaign.expiredAt) : null;
        });
        return res;
    }
}
