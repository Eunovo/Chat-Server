import mongoose from 'mongoose';

mongoose.connection.on("connected", () => {
    console.log("Database connected successfully");
});
mongoose.connection.on("disconnected", () => {
    console.log("Database disconnected successfully");
});

export default function connect(dbUrl: string) {
    mongoose.connect(dbUrl || process.env.DB_URL, {
        useNewUrlParser: true,
        useUnifiedTopology: true
    });
}

export function disconnect() {
    return mongoose.disconnect();
}