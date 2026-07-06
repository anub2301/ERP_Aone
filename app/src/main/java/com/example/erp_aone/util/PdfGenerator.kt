package com.example.erp_aone.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.erp_aone.data.entity.BillEntity
import com.example.erp_aone.data.entity.BillLineEntity
import java.io.File
import java.io.FileOutputStream

object PdfGenerator {
    fun generateInvoice(
        context: Context,
        bill: BillEntity,
        lines: List<BillLineEntity>
    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        // Header - Branding
        paint.color = Color.parseColor("#1B5E20") // ERP Primary Green
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText("ERP_Aone", 40f, 60f, paint)

        paint.color = Color.DKGRAY
        paint.textSize = 12f
        paint.isFakeBoldText = false
        canvas.drawText("Simplify Your Success", 40f, 80f, paint)

        // Invoice Info
        paint.color = Color.BLACK
        paint.textSize = 14f
        paint.isFakeBoldText = true
        canvas.drawText("TAX INVOICE", 400f, 60f, paint)
        paint.textSize = 12f
        paint.isFakeBoldText = false
        canvas.drawText("Bill No: ${bill.id}", 400f, 80f, paint)
        canvas.drawText("Date: ${bill.date}", 400f, 100f, paint)

        // Customer Info
        paint.color = Color.LTGRAY
        canvas.drawRect(40f, 120f, 555f, 122f, paint)
        paint.color = Color.BLACK
        paint.isFakeBoldText = true
        canvas.drawText("Bill To:", 40f, 145f, paint)
        paint.isFakeBoldText = false
        canvas.drawText(bill.customerName, 40f, 165f, paint)
        canvas.drawText(bill.mobile, 40f, 185f, paint)
        canvas.drawText(bill.address, 40f, 205f, paint)

        // Table Header
        paint.color = Color.LTGRAY
        canvas.drawRect(40f, 230f, 555f, 255f, paint)
        paint.color = Color.BLACK
        paint.isFakeBoldText = true
        canvas.drawText("S.N.", 45f, 247f, paint)
        canvas.drawText("Description", 80f, 247f, paint)
        canvas.drawText("Qty", 350f, 247f, paint)
        canvas.drawText("Price", 420f, 247f, paint)
        canvas.drawText("Total", 500f, 247f, paint)

        // Items
        paint.isFakeBoldText = false
        var yPos = 275f
        lines.forEachIndexed { index, item ->
            canvas.drawText((index + 1).toString(), 45f, yPos, paint)
            canvas.drawText(item.itemName, 80f, yPos, paint)
            canvas.drawText(item.qty.toString(), 350f, yPos, paint)
            canvas.drawText(item.price.toString(), 420f, yPos, paint)
            canvas.drawText(((item.price * item.qty) - item.discount).toString(), 500f, yPos, paint)
            yPos += 20f
        }

        // Totals
        paint.color = Color.LTGRAY
        canvas.drawRect(40f, 750f, 555f, 752f, paint)
        paint.color = Color.BLACK
        paint.isFakeBoldText = true
        canvas.drawText("Taxable Amount:", 350f, 770f, paint)
        canvas.drawText(bill.taxableAmount.toString(), 500f, 770f, paint)
        canvas.drawText("GST:", 350f, 790f, paint)
        canvas.drawText(bill.totalTax.toString(), 500f, 790f, paint)
        canvas.drawText("Grand Total:", 350f, 810f, paint)
        paint.color = Color.parseColor("#1B5E20")
        canvas.drawText("₹${bill.grandTotal}", 500f, 810f, paint)

        pdfDocument.finishPage(page)

        val file = File(context.cacheDir, "Invoice_${bill.id}.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            pdfDocument.close()
        }
        return file
    }
}
