import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IVasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';

type EntityResponseType = HttpResponse<IVasCloudConfiguration>;
type EntityArrayResponseType = HttpResponse<IVasCloudConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class VasCloudConfigurationService {
    private resourceUrl = SERVER_API_URL + 'api/vas-cloud-configurations';
    // Entity and Entities shared
    public entity: IVasCloudConfiguration;
    public entities: IVasCloudConfiguration[];
    // Temporary data used for store object on single and multiple select box
    public selected;
    public checked: string[] = [];

    constructor(private http: HttpClient) {}

    create(vasCloudConfiguration: IVasCloudConfiguration): Observable<EntityResponseType> {
        return this.http.post<IVasCloudConfiguration>(this.resourceUrl, vasCloudConfiguration, { observe: 'response' });
    }

    update(vasCloudConfiguration: IVasCloudConfiguration): Observable<EntityResponseType> {
        return this.http.put<IVasCloudConfiguration>(this.resourceUrl, vasCloudConfiguration, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IVasCloudConfiguration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IVasCloudConfiguration[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
