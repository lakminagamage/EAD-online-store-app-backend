import { Request, Response, NextFunction } from 'express';
import jwt from 'jsonwebtoken';

const authMiddleware = (req: Request, res: Response, next: NextFunction): void => {
    const token = req.header('Authorization')?.replace('Bearer ', '');
    if (!token) {
        res.status(401).send({ error: 'Access denied. No token provided.' });
        return;
    }

    try {
        jwt.verify(token, process.env.JWT_SECRET as string);
        next();
    } catch (ex) {
        if (ex instanceof jwt.TokenExpiredError) {
            res.status(401).send({ error: 'Access denied. Token has expired.' });
            return;
        }
        res.status(400).send({ error: 'Invalid token.' });
    }
};

export default authMiddleware;
