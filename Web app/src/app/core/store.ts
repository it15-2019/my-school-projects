import { BehaviorSubject, Observable } from "rxjs";
import { map } from "rxjs/operators";
import { distinctUntilChanged } from "rxjs/operators";

export class Store<T>
{
    state$!: Observable<T>;
    private _state$!: BehaviorSubject<T>;

    constructor(initialState: T) 
    {
        this._state$ = new BehaviorSubject<T>(initialState);
        this.state$ = this._state$.asObservable();
    }

    select<T>(selectorFunction: any) : Observable<T>
    {
        return this.state$.pipe(
            distinctUntilChanged(),
            map(selectorFunction));
    }

    // sync
    get state()
    {
        return this._state$.getValue();
    }

    protected setState (nextState : T) : void 
    {
        console.log('_________________');
        console.log('Previous state:', this.state);
        this._state$.next(nextState);

        console.log('Current state:', this.state);
        console.log('_________________');
    }

}