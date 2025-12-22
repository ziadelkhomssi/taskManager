import { Observable } from "rxjs";
import { PageUserSummary } from "../../core/ng-openapi";

export interface PageQuery {
	page: number,
	size: number,
	search: string,
	filter: string
}
