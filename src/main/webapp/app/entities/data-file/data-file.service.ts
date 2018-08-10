import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDataFile } from 'app/shared/model/data-file.model';

type EntityResponseType = HttpResponse<IDataFile>;
type EntityArrayResponseType = HttpResponse<IDataFile[]>;
// File Save support
import { saveAs } from 'file-saver';

@Injectable({ providedIn: 'root' })
export class DataFileService {
    private resourceUrl = SERVER_API_URL + 'api/data-files';
    public exportUrl = 'api/data-files/export';
    public importUrl = 'api/data-files/import';
    // Entity and Entities shared
    public entity: IDataFile;
    public entities: IDataFile[];
    // Temporary data used for store object on single and multiple select box
    public selected;
    public checked: any = {};
    // Pagination and sorting
    public totalItems: any;
    public queryCount: any;
    public itemsPerPage: any;
    public page: any;
    public predicate: any;
    public previousPage: any;
    public reverse: any;
    public links: any;
    public dataCsvHeaders: string[];

    constructor(private http: HttpClient) {}

    create(dataFile: IDataFile): Observable<EntityResponseType> {
        return this.http.post<IDataFile>(this.resourceUrl, dataFile, { observe: 'response' });
    }

    update(dataFile: IDataFile): Observable<EntityResponseType> {
        return this.http.put<IDataFile>(this.resourceUrl, dataFile, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IDataFile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDataFile[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    /**
     * Export data into file
     */
    getExportUrl(): string {
        return SERVER_API_URL + this.exportUrl;
    }
    getImportUrl(): string {
        return SERVER_API_URL + this.importUrl;
    }

    exportData() {
        this.http
            .get(this.getExportUrl(), { responseType: 'text' })
            .subscribe(res =>
                saveAs(new Blob([res], { type: 'text;charset=utf-8' }), 'whitelist.' + new Date().toISOString().substr(0, 10) + '.url')
            );
    }

    saveData() {
        return this.http.post(this.getExportUrl(), { observe: 'response' });
    }

    // Send a PUT request to import endpoint to reload data from static file.
    reloadData() {
        return this.http.put<IDataFile>(this.getImportUrl(), null, { observe: 'response' });
    }
}
