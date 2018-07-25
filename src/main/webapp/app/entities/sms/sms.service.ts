import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISms } from 'app/shared/model/sms.model';

type EntityResponseType = HttpResponse<ISms>;
type EntityArrayResponseType = HttpResponse<ISms[]>;

@Injectable({ providedIn: 'root' })
export class SmsService {
    private resourceUrl = SERVER_API_URL + 'api/sms';

    constructor(private http: HttpClient) {}

    create(sms: ISms): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sms);
        return this.http
            .post<ISms>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(sms: ISms): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sms);
        return this.http
            .put<ISms>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ISms>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISms[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(sms: ISms): ISms {
        const copy: ISms = Object.assign({}, sms, {
            submitAt: sms.submitAt != null && sms.submitAt.isValid() ? sms.submitAt.toJSON() : null,
            expiredAt: sms.expiredAt != null && sms.expiredAt.isValid() ? sms.expiredAt.toJSON() : null,
            deliveredAt: sms.deliveredAt != null && sms.deliveredAt.isValid() ? sms.deliveredAt.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.submitAt = res.body.submitAt != null ? moment(res.body.submitAt) : null;
        res.body.expiredAt = res.body.expiredAt != null ? moment(res.body.expiredAt) : null;
        res.body.deliveredAt = res.body.deliveredAt != null ? moment(res.body.deliveredAt) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((sms: ISms) => {
            sms.submitAt = sms.submitAt != null ? moment(sms.submitAt) : null;
            sms.expiredAt = sms.expiredAt != null ? moment(sms.expiredAt) : null;
            sms.deliveredAt = sms.deliveredAt != null ? moment(sms.deliveredAt) : null;
        });
        return res;
    }
}
